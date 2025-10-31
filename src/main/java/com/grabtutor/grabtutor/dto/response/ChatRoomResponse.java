package com.grabtutor.grabtutor.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
public class ChatRoomResponse {
    String id;
    String postId;
    boolean chatEnabled;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
