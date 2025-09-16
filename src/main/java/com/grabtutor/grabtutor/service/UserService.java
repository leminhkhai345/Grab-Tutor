package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.TutorInfoRequest;
import com.grabtutor.grabtutor.dto.request.UserRequest;
import com.grabtutor.grabtutor.dto.request.AccountVerificationRequest;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.dto.response.TutorInfoResponse;
import com.grabtutor.grabtutor.dto.response.UserResponse;
import com.grabtutor.grabtutor.dto.response.AccountVerificationResponse;


public interface UserService {
    UserResponse addUser(UserRequest userRequest);

    UserResponse getUserById(String id);

    UserResponse updateUser(String id, UserRequest userRequest);

    void deleteUser(String id);

    UserResponse changeActive(String id, boolean active);

    PageResponse<?> getAllUsers(int pageNo, int pageSize, String... sortBy);

    UserResponse getMyInfo();

    TutorInfoResponse addInfo(TutorInfoRequest tutorInfoRequest);

    AccountVerificationResponse verifyTutor(AccountVerificationRequest request);
}
