package com.grabtutor.grabtutor.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
public class PostResponse {
    String id;
    String imageUrl;
    String description;
    boolean isDeleted;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String status;
}
