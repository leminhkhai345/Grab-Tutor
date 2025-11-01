package com.grabtutor.grabtutor.dto.response;

import com.grabtutor.grabtutor.entity.BaseEntity;
import com.grabtutor.grabtutor.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse extends BaseEntity {
    String id;
    String userId;
    MessageType type;
    String title;
    String content;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
