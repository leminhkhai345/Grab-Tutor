package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.UserRequest;
import com.grabtutor.grabtutor.dto.response.UserResponse;
import com.grabtutor.grabtutor.entity.User;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;

import java.util.List;

public interface UserService {
    public UserResponse addUser(UserRequest userRequest);


    public List<UserResponse> getAllUsers();
    public UserResponse getUserById(String id);

    public UserResponse updateUser(String id, UserRequest userRequest);

    public void deleteUser(String id);

    public UserResponse changeActive(String id, boolean active);
}
