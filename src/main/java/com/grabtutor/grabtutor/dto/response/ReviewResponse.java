package com.grabtutor.grabtutor.dto.response;

import com.grabtutor.grabtutor.entity.Post;
import com.grabtutor.grabtutor.entity.User;
import jakarta.persistence.ManyToOne;
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
    int rating;
    String description;
    String userId;
    Post post;

}
