package com.grabtutor.grabtutor.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grabtutor.grabtutor.dto.request.MessageRequest;
import com.grabtutor.grabtutor.dto.response.MessageResponse;
import com.grabtutor.grabtutor.dto.response.SignalResponse;
import com.grabtutor.grabtutor.entity.Notification;
import com.grabtutor.grabtutor.enums.MessageType;
import com.grabtutor.grabtutor.mapper.MessageMapper;
import com.grabtutor.grabtutor.mapper.NotificationMapper;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)

public class TcpSessionRegistry {

    Map<String, Set<SocketWrapper>> userSessions = new ConcurrentHashMap<>();
    Map<String, Set<SocketWrapper>> roomSessions = new ConcurrentHashMap<>();
    ObjectMapper objectMapper;
    NotificationMapper notificationMapper;

    public void register(SocketWrapper wrapper, String userId) {
        wrapper.setUserId(userId);
        userSessions.computeIfAbsent(userId, k -> new CopyOnWriteArraySet<>()).add(wrapper);
        log.info("User {} registered on path {}", userId, wrapper.getPath());
    }

    public void remove(SocketWrapper wrapper) {
        if (wrapper.getUserId() != null) {
            Set<SocketWrapper> userSet = userSessions.get(wrapper.getUserId());
            if (userSet != null) {
                userSet.remove(wrapper);
                if (userSet.isEmpty()) userSessions.remove(wrapper.getUserId());
            }
        }
        if (wrapper.getRoomId() != null) {
            Set<SocketWrapper> roomSet = roomSessions.get(wrapper.getRoomId());
            if (roomSet != null) roomSet.remove(wrapper);
        }
        wrapper.close();
        log.info("Session removed for user {}", wrapper.getUserId());
    }

    public void joinRoom(SocketWrapper wrapper, String roomId) {
        // Rời phòng cũ nếu có
        if (wrapper.getRoomId() != null) {
            Set<SocketWrapper> oldRoom = roomSessions.get(wrapper.getRoomId());
            if (oldRoom != null) oldRoom.remove(wrapper);
        }
        wrapper.setRoomId(roomId);
        roomSessions.computeIfAbsent(roomId, k -> new CopyOnWriteArraySet<>()).add(wrapper);
        log.info("User {} joined room {}", wrapper.getUserId(), roomId);
    }

    public void sendMessageToRoom(String roomId, MessageResponse message) {
        Set<SocketWrapper> sessions = roomSessions.get(roomId);
        if (sessions != null && !sessions.isEmpty()) {
            String json = serialize(message);

            sessions.forEach(s -> s.send(json));
        }
    }

    public void sendNotificationToUser(String userId, Notification notification) {
        Set<SocketWrapper> sessions = userSessions.get(userId);
        if (sessions != null && !sessions.isEmpty()) {
            String json = serialize(notificationMapper.toNotificationResponse(notification));

            sessions.forEach(s -> s.send(json));
        }
    }
    public void sendSignalToRoom(String roomId, MessageType type, String title, String message) {
        Set<SocketWrapper> sessions = roomSessions.get(roomId);
        if (sessions == null) {
            log.warn("No sessions found for room {}", roomId);
            return;
        }
        var signal = SignalResponse.builder()
                .type(type)
                .title(title)
                .message(message)
                .build();
        String signalJson = serialize(signal);
        sessions.forEach(s -> s.send(signalJson));
    }

    private String serialize(Object obj) {
        try { return objectMapper.writeValueAsString(obj); }
        catch (Exception e) { return "{}"; }
    }
}