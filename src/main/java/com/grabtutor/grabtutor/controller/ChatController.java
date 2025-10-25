package com.grabtutor.grabtutor.controller;

import com.grabtutor.grabtutor.dto.request.MessageRequest;
import com.grabtutor.grabtutor.mapper.MessageMapper;
import com.grabtutor.grabtutor.service.impl.ChatRoomServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {
    SimpMessagingTemplate messagingTemplate;
    ChatRoomServiceImpl chatRoomService;
    MessageMapper  messageMapper;

    // Gửi tin vào kênh cụ thể (ví dụ: /topic/room1)
    @MessageMapping("/chat.send/{roomId}")
    @SendTo("/topic/{roomId}")
    public void sendToChannel(MessageRequest message) {
        log.debug("Sending message to room {}", message.getRoomId());
        messagingTemplate.convertAndSend("/topic/" + message.getRoomId(), messageMapper.ToMessageResponse(message));
//        return chatRoomService.saveMessage(message);
    }

    // Gửi thông báo riêng cho 1 user
    @MessageMapping("/notify.user")
    public void notifyUser(MessageRequest request) {
        messagingTemplate.convertAndSendToUser(
                request.getUserId(),
                "/queue/notify",
                request
        );
    }
}
