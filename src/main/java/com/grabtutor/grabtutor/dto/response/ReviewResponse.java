package com.grabtutor.grabtutor.dto.response;


import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
public class ReviewResponse {
    String id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    boolean isDeleted;
    int stars;
    String description;
    String senderId;
    String receiverId;
    String postId;

}
