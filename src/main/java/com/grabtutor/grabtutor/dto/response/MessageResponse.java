package com.grabtutor.grabtutor.dto.response;

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
    String userId;
    String roomId;
    String message;
    String fileName;
    String fileUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
