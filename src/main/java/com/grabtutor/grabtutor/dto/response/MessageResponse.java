package com.grabtutor.grabtutor.dto.response;

import com.grabtutor.grabtutor.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    String id;
    String userId;
    MessageType type;
    String email;
    String roomId;
    String message;
    String fileName;
    String fileUrl;
    boolean isDeleted;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
