package com.grabtutor.grabtutor.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
public class PostResponse {
    String id;
    String imageUrl;
    String description;
    boolean isDeleted;
    LocalDate createdAt;
    LocalDate updatedAt;
}
