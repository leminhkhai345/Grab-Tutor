package com.grabtutor.grabtutor.websocket;

import com.grabtutor.grabtutor.dto.response.NotificationResponse;
import com.grabtutor.grabtutor.entity.Notification;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.repository.NotificationRepository;
import com.grabtutor.grabtutor.repository.UserRepository;
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
    UserRepository userRepository;
    NotificationRepository notificationRepository;

    /**
     * Gọi hàm này từ bất kỳ đâu để gửi thông báo
     * @param userId Id của người cần nhận
     * @param message Nội dung thông báo
     */
    public void sendNotification(String userId, String title, String message) {
        // Xây dựng một DTO chuẩn cho thông báo
        Map<String, String> notificationDto = Map.of(
                "type", "NOTIFICATION",
                "content", message
        );
        var user = userRepository.findById(userId).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        var notification = Notification.builder()
                .user(user)
                .title(title)
                .content(message)
                .build();
        notificationRepository.save(notification);
        sessionRegistry.sendNotificationToUser(userId, notification);
    }

}