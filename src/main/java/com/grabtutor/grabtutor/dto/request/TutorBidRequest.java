package com.grabtutor.grabtutor.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TutorBidRequest {
    double proposedPrice;
    String questionLevel;
    String description;
    String postId;
}
