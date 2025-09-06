package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.UserRequest;
import com.grabtutor.grabtutor.dto.response.UserResponse;
import com.grabtutor.grabtutor.entity.User;
import com.grabtutor.grabtutor.mapper.UserMapper;
import com.grabtutor.grabtutor.repository.UserRepository;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public UserResponse addUser(UserRequest userRequest){
        if(userRepository.existsByUsername(userRequest.getUsername())){
            throw new IllegalArgumentException("Username already exists");
        }
        User user = userMapper.toUser(userRequest);
        return userMapper.toUserResponse(userRepository.save(user));
    }
}
