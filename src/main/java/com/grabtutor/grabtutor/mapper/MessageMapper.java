package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.request.MessageRequest;
import com.grabtutor.grabtutor.dto.response.MessageResponse;
import com.grabtutor.grabtutor.entity.Message;
import com.grabtutor.grabtutor.enums.MessageType;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    default MessageResponse ToMessageResponse(Message message){
        if(message == null){
            return null;
        }
        return MessageResponse.builder()
                .roomId(message.getChatRoom().getId())
                .userId(message.getUser().getId())
                .fileUrl(message.getFileUrl())
                .fileName(message.getFileName())
                .type(MessageType.MESSAGE)
                .updatedAt(message.getUpdatedAt())
                .createdAt(message.getCreatedAt())
                .message(message.getMessage())
                .email(message.getUser().getEmail())
                .build();
    }
    Message ToMessage(MessageRequest message);
    default MessageResponse ToMessageResponse(MessageRequest message){
        if(message == null){
            return null;
        }
        return MessageResponse.builder()
                .roomId(message.getRoomId())
                .userId(message.getUserId())
                .message(message.getMessage())
                .type(message.getType())
                .fileName(message.getFileName())
                .fileUrl(message.getFileUrl())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
