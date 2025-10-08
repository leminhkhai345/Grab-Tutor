package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.request.MessageRequest;
import com.grabtutor.grabtutor.dto.response.MessageResponse;
import com.grabtutor.grabtutor.entity.Message;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageResponse ToMessageResponse(Message message);
    Message ToMessage(MessageRequest message);
    default MessageResponse ToMessageResponse(MessageRequest message){
        if(message == null){
            return null;
        }
        return MessageResponse.builder()
                .roomId(message.getRoomId())
                .userId(message.getUserId())
                .message(message.getMessage())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
