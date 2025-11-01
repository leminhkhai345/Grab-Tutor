package com.grabtutor.grabtutor.websocket;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grabtutor.grabtutor.dto.request.MessageRequest;
import com.grabtutor.grabtutor.dto.response.MessageResponse;
import com.grabtutor.grabtutor.entity.Notification;
import com.grabtutor.grabtutor.mapper.MessageMapper;
import com.grabtutor.grabtutor.mapper.NotificationMapper;
import com.grabtutor.grabtutor.service.ChatRoomService;
import com.grabtutor.grabtutor.service.impl.ChatRoomServiceImpl;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@Getter
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketSessionRegistry {

    // Map<userId, Set<Session>>: Quản lý đa tab
    Map<String, Set<Session>> userSessions = new ConcurrentHashMap<>();

    //Set<Session>>: Quản lý phòng chat
    Map<String, Set<Session>> roomSessions = new ConcurrentHashMap<>();

    ObjectMapper objectMapper;

    MessageMapper messageMapper;
    NotificationMapper notificationMapper;
    ChatRoomService chatRoomService;

    /**
     * Khi user kết nối (mở tab)
     */
    public void registerSession(Session session, String userId) {
        session.getUserProperties().put("userId", userId);
        userSessions.computeIfAbsent(userId, k -> new CopyOnWriteArraySet<>()).add(session);
        log.info("User {} connected with session {}", userId, session.getId());
    }

    /**
     * Khi user đóng tab
     */
    public void removeSession(Session session) {
        String userId = (String) session.getUserProperties().get("userId");
        if (userId != null) {
            Set<Session> userSet = userSessions.get(userId);
            if (userSet != null) {
                userSet.remove(session);
                if (userSet.isEmpty()) {
                    userSessions.remove(userId);
                }
            }
            log.info("User {} disconnected from session {}", userId, session.getId());
        }

        // Xóa session khỏi phòng chat (nếu có)
        String roomId = (String) session.getUserProperties().get("roomId");
        if (roomId != null) {
            Set<Session> roomSet = roomSessions.get(roomId);
            if (roomSet != null) {
                roomSet.remove(session);
                if (roomSet.isEmpty()) {
                    roomSessions.remove(roomId);
                }
            }
            log.info("Session {} removed from room {}", session.getId(), roomId);
        }
    }

    /**
     * Khi user click vào một phòng chat (gửi tin JOIN_ROOM)
     */
    public void joinRoom(Session session, String roomId) {
        // 1. Rời phòng cũ (nếu có)
        String oldRoomId = (String) session.getUserProperties().get("roomId");
        if (oldRoomId != null) {
            roomSessions.getOrDefault(oldRoomId, new CopyOnWriteArraySet<>()).remove(session);
            if (roomSessions.getOrDefault(oldRoomId, Set.of()).isEmpty()) {
                roomSessions.remove(oldRoomId);
            }
        }

        // 2. Lưu thông tin phòng mới vào session
        session.getUserProperties().put("roomId", roomId);

        // 3. Thêm session vào phòng mới
        roomSessions.computeIfAbsent(roomId, k -> new CopyOnWriteArraySet<>()).add(session);
        log.info("Session {} joined room {}", session.getId(), roomId);
    }

    /**
     * Gửi tin nhắn chat đến tất cả user trong phòng
     */
    public void sendMessageToRoom(String roomId, MessageRequest messageDto) {
        Set<Session> sessionsInRoom = roomSessions.get(roomId);
        if (sessionsInRoom == null) {
            log.warn("No sessions found for room {}", roomId);
            return;
        }
        var response = chatRoomService.saveMessage(messageDto);
        String messageJson = serializeMessage(response);
        sessionsInRoom.forEach(session -> sendMessage(session, messageJson));
    }

    /**
     * Gửi thông báo đến TẤT CẢ các tab của 1 user
     */
    public void sendNotificationToUser(String userId, Notification notification) {
        Set<Session> sessionsForUser = userSessions.get(userId);
        if (sessionsForUser == null) {
            log.warn("No sessions found for user {}", userId);
            return;
        }
        String notificationJson = serializeMessage(notificationMapper.toNotificationResponse(notification));
        sessionsForUser.forEach(session -> sendMessage(session, notificationJson));
    }

    // --- Helper Methods ---
    private void sendMessage(Session session, String messageJson) {
        if (session.isOpen()) {
            session.getAsyncRemote().sendText(messageJson);
        }
    }

    private String serializeMessage(Object messageDto) {
        try {
            return objectMapper.writeValueAsString(messageDto);
        } catch (IOException e) {
            log.error("Failed to serialize message", e);
            return "{\"type\":\"ERROR\", \"content\":\"Internal server error\"}";
        }
    }
}