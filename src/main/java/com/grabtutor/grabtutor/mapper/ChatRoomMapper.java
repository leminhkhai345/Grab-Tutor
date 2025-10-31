package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.response.ChatRoomResponse;
import com.grabtutor.grabtutor.entity.ChatRoom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {
    default ChatRoomResponse toChatRoomResponse(ChatRoom chatRoom){
        return ChatRoomResponse.builder()
                .chatEnabled(chatRoom.isChatEnabled())
                .createdAt(chatRoom.getCreatedAt())
                .updatedAt(chatRoom.getUpdatedAt())
                .id(chatRoom.getId())
                .postId(chatRoom.getPost().getId())
                .build();
    }
}
