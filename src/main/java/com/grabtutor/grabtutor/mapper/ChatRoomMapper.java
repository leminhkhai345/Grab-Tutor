package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.response.ChatRoomResponse;
import com.grabtutor.grabtutor.entity.ChatRoom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {
    ChatRoomResponse toChatRoomResponse(ChatRoom chatRoom);
}
