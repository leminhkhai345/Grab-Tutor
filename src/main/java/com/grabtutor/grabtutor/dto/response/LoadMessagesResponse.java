package com.grabtutor.grabtutor.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class LoadMessagesResponse {
    List<MessageResponse> messages;
}
