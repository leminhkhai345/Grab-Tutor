package com.grabtutor.grabtutor.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.util.List;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
public class LoadChatRoomsResponse {
    List<ChatRoomResponse> rooms;
}
