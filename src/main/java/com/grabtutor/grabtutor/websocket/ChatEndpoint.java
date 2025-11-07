//package com.grabtutor.grabtutor.websocket;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.grabtutor.grabtutor.dto.request.MessageRequest;
//import com.grabtutor.grabtutor.enums.MessageType;
//import com.grabtutor.grabtutor.mapper.MessageMapper;
//import com.grabtutor.grabtutor.service.ChatRoomService;
//import com.grabtutor.grabtutor.websocket.AuthHandshakeConfigurator;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import jakarta.websocket.*;
//import jakarta.websocket.server.ServerEndpoint;
//import java.io.IOException;
//
//@Component
//@Slf4j
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@RequiredArgsConstructor
//// 1. Dùng URL /ws/chat
//// 2. Chỉ định Configurator đã tạo
//@ServerEndpoint(value = "/ws/chat", configurator = AuthHandshakeConfigurator.class)
//public class ChatEndpoint {
//
//    // === Static Injection Hack (bắt buộc) ===
//    static WebSocketSessionRegistry sessionRegistry;
//
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    ChatRoomService chatRoomService;
//    @Autowired
//    public void setSessionRegistry(WebSocketSessionRegistry registry) {
//        ChatEndpoint.sessionRegistry = registry;
//    }
//    // =====================================
//
//    @OnOpen
//    public void onOpen(Session session) throws IOException {
//        // Lấy userId từ "userProperties" (đã được Configurator xác thực)
//        String userId = (String) session.getUserProperties().get("userId");
//
//        // Kiểm tra bảo mật
//        if (userId == null) {
//            log.warn("Unauthenticated connect attempt closed.");
//            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Authentication failed"));
//            return;
//        }
//
//        log.info("onOpen: {} for user: {}", session.getId(), userId);
//        sessionRegistry.registerSession(session, userId);
//    }
//
//    @OnMessage
//    public void onMessage(Session session, String message) throws IOException {
//        log.info("onMessage: {}", message);
//        MessageRequest request = objectMapper.readValue(message, MessageRequest.class);
//
//        switch (request.getType()) {
//            case MessageType.JOIN:
//                sessionRegistry.joinRoom(session, request.getRoomId());
//                break;
//
//            case MessageType.MESSAGE:
//                // Gửi toàn bộ object JSON cho các thành viên
//                var response = chatRoomService.saveMessage(request);
//                sessionRegistry.sendMessageToRoom(request.getRoomId(), response);
//                break;
//
//            default:
//                log.warn("Unknown message type: {}", request.getType());
//        }
//    }
//
//    @OnClose
//    public void onClose(Session session) {
//        log.info("onClose: {}", session.getId());
//        sessionRegistry.removeSession(session);
//    }
//
//    @OnError
//    public void onError(Session session, Throwable throwable) {
//        log.error("onError: {}", session.getId(), throwable);
//        sessionRegistry.removeSession(session);
//    }
//
//}