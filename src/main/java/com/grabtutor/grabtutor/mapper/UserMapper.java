package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.request.UserRequest;
import com.grabtutor.grabtutor.dto.response.UserResponse;
import com.grabtutor.grabtutor.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest userRequest);
    UserResponse toUserResponse(User user);
}
