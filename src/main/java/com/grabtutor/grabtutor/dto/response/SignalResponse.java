package com.grabtutor.grabtutor.dto.response;

import com.grabtutor.grabtutor.enums.MessageType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
public class SignalResponse {
    String title;
    String message;
    MessageType type;
}
