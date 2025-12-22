package com.grabtutor.grabtutor.socket;

import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.entity.Notification;
import com.grabtutor.grabtutor.enums.MessageType;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.NotificationMapper;
import com.grabtutor.grabtutor.repository.NotificationRepository;
import com.grabtutor.grabtutor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {

    TcpSessionRegistry sessionRegistry;
    UserRepository userRepository;
    NotificationRepository notificationRepository;
    NotificationMapper notificationMapper;

    public void sendNotification(String userId, String title, String message, String refId) {
        var user = userRepository.findById(userId).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        var notification = Notification.builder()
                .user(user)
                .title(title)
                .content(message)
                .refId(refId)
                .build();
        notificationRepository.save(notification);
        sessionRegistry.sendNotificationToUser(userId, notification);
    }
    public void sendSignal(String roomId, MessageType type,String title , String message) {
        sessionRegistry.sendSignalToRoom(roomId, type, title, message);
    }
    public PageResponse<?> getNotificationByUserId(String userId , int pageNo, int pageSize, String... sorts) {
        List<Sort.Order> orders = new ArrayList<>();
        for(String sortBy : sorts){
            // firstname:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()){
                if(matcher.group(3).equalsIgnoreCase("desc")){
                    orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                }
            }

        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(orders));
        Page<Notification> notifications = notificationRepository.findAllByIsDeletedFalseAndUserId(userId, pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(notifications.getTotalPages())
                .items(notifications.stream().map(notificationMapper::toNotificationResponse).toList())
                .build();
    }

}