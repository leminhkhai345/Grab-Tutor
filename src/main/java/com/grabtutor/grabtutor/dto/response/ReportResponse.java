package com.grabtutor.grabtutor.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


import java.time.LocalDateTime;
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
public class ReportResponse {
    String id;
    String reportStatus;
    String detail;
    String senderId;
    String receiverId;
    String postId;
    String chatRoomId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    boolean isDeleted;
}
