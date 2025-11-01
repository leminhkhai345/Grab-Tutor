package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.response.NotificationResponse;
import com.grabtutor.grabtutor.entity.Notification;
import com.grabtutor.grabtutor.enums.MessageType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    default NotificationResponse toNotificationResponse(Notification notification){
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .userId(notification.getUser().getId())
                .type(MessageType.NOTIFICATION)
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .build();
    }
}
