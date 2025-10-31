package com.grabtutor.grabtutor.dto.response;

import com.grabtutor.grabtutor.entity.Post;
import com.grabtutor.grabtutor.entity.User;
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
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    boolean isDeleted;
}
