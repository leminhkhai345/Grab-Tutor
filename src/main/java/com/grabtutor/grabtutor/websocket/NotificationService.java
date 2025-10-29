package com.grabtutor.grabtutor.websocket;

import com.grabtutor.grabtutor.websocket.WebSocketSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {

    WebSocketSessionRegistry sessionRegistry;

    /**
     * Gọi hàm này từ bất kỳ đâu để gửi thông báo
     * @param userId Id của người cần nhận
     * @param message Nội dung thông báo
     */
    public void sendNotification(String userId, String message) {
        // Xây dựng một DTO chuẩn cho thông báo
        Map<String, String> notificationDto = Map.of(
                "type", "GLOBAL_NOTIFICATION",
                "content", message
        );

        sessionRegistry.sendNotificationToUser(userId, notificationDto);
    }
}