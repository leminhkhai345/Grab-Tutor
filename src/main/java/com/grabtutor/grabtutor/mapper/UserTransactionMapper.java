package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.response.UserTransactionResponse;
import com.grabtutor.grabtutor.entity.UserTransaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserTransactionMapper {
    default UserTransactionResponse toUserTransactionResponse(UserTransaction userTransaction){
        if(userTransaction == null){
            return null;
        }
        return UserTransactionResponse.builder()
                .id(userTransaction.getId())
                .amount(userTransaction.getAmount())
                .status(userTransaction.getStatus())
                .type(userTransaction.getType())
                .createdAt(userTransaction.getCreatedAt())
                .postId(userTransaction.getPost() == null ? null : userTransaction.getPost().getId())
                .courseId(userTransaction.getCourse() == null ? null : userTransaction.getCourse().getId())
                .senderId(userTransaction.getSender().getId())
                .receiverId(userTransaction.getReceiver().getId())
                .build();
    }
}
