package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.request.UserRequest;
import com.grabtutor.grabtutor.dto.response.UserResponse;
import com.grabtutor.grabtutor.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest userRequest);
    void updateUserFromRequest(UserRequest userRequest,@MappingTarget User user);

    default UserResponse toUserResponse(User user){
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id(user.getId() );
        userResponse.username(user.getUsername() );
        userResponse.dob( user.getDob() );
        userResponse.email( user.getEmail() );
        userResponse.phoneNumber( user.getPhoneNumber() );
        userResponse.userStatus( user.getUserStatus() );
        userResponse.createdAt( user.getCreatedAt() );
        userResponse.updatedAt( user.getUpdatedAt() );
        userResponse.isActive(user.isActive());
        userResponse.roles( user.getRoles() );

        return userResponse.build();
    }
}
