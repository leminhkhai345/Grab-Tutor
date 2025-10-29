package com.grabtutor.grabtutor.mapper;


import com.grabtutor.grabtutor.dto.request.TutorRequest;
import com.grabtutor.grabtutor.dto.request.UserRequest;
import com.grabtutor.grabtutor.dto.request.UserUpdateRequest;
import com.grabtutor.grabtutor.dto.response.UserResponse;
import com.grabtutor.grabtutor.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest userRequest);
    default void updateUserFromRequest(UserUpdateRequest userRequest, @MappingTarget User user){
        if ( userRequest == null ) {
            return;
        }

        if ( userRequest.getFullName() != null && !userRequest.getFullName().isEmpty() ) {
            user.setFullName( userRequest.getFullName() );
        }
        if ( userRequest.getDob() != null && !userRequest.getDob().toString().isEmpty() ) {
            user.setDob( userRequest.getDob() );
        }
        if ( userRequest.getPhoneNumber() != null && !userRequest.getPhoneNumber().isEmpty() ) {
            user.setPhoneNumber( userRequest.getPhoneNumber() );
        }
    }
    User toUser(TutorRequest tutorRequest);
    default UserResponse toUserResponse(User user){
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id(user.getId() );
        userResponse.dob( user.getDob() );
        userResponse.email( user.getEmail() );
        userResponse.fullName( user.getFullName() );
        userResponse.phoneNumber( user.getPhoneNumber() );
        userResponse.userStatus( user.getUserStatus() );
        userResponse.createdAt( user.getCreatedAt() );
        userResponse.updatedAt( user.getUpdatedAt() );
        userResponse.isActive(user.isActive());
        userResponse.role( user.getRole().toString() );

        return userResponse.build();
    }
}
