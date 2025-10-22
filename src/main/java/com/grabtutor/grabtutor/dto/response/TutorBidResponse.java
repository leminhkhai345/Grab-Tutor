package com.grabtutor.grabtutor.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TutorBidResponse {
    String id;
    double proposedPrice;
    String questionLevel;
    String description;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
