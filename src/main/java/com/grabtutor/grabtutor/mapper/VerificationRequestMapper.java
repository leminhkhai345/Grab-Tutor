package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.request.AccountVerificationRequest;
import com.grabtutor.grabtutor.dto.response.AccountVerificationResponse;
import com.grabtutor.grabtutor.entity.VerificationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VerificationRequestMapper {
    default AccountVerificationResponse toAccountVerificationResponse(VerificationRequest request){
        if(request == null){
            return null;
        }
        return AccountVerificationResponse.builder()
                .requestId(request.getId())
                .userId(request.getUser().getId())
                .status(request.getStatus().toString())
                .build();
    }
}
