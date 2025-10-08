package com.grabtutor.grabtutor.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class MessageRequest {
    private String userId;
    private String roomId;
    private String message;
}
