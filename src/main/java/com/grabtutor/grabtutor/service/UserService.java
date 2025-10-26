package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.*;
import com.grabtutor.grabtutor.dto.response.*;

import java.util.List;


public interface UserService {
    UserResponse addUser(UserRequest userRequest);

    UserResponse getUserById(String id);

    UserResponse updateUser(String id, UserRequest userRequest);

    void deleteUser(String id);

    UserResponse changeActive(String id, boolean active);

    PageResponse<?> getAllUsers(int pageNo, int pageSize, String... sortBy);

    UserResponse getMyInfo();
    TutorResponse addTutor(TutorRequest request);

    TutorResponse addTutor(TutorRequest request);

    TutorInfoResponse updateTutorInfo(TutorInfoRequest tutorInfoRequest);

    ApproveResponse approveRequest(ApproveRequest request);

    RejectResponse rejectRequest(RejectRequest request);

    PageResponse<?> getRequests(int pageNo, int pageSize,String... sorts);

    VirtualTransactionResponse withdrawMoney(double withdrawAmount);
}
