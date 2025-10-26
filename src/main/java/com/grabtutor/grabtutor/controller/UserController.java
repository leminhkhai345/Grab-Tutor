package com.grabtutor.grabtutor.controller;

import com.grabtutor.grabtutor.dto.request.*;
import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.entity.TutorInfo;
import com.grabtutor.grabtutor.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    @PostMapping
    public ApiResponse<?> createUser(@RequestBody @Valid UserRequest userRequest){
        return ApiResponse.builder()
                .message("User created successfully")
                .data(userService.addUser(userRequest))
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<?> updateUser(@PathVariable String userId,
                                     @RequestBody @Valid UserRequest userRequest){
        return ApiResponse.builder()
                .message("User updated successfully")
                .data(userService.updateUser(userId, userRequest))
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<?> getUserById(@PathVariable String userId){
        return ApiResponse.builder()
                .message("get user by id successfully")
                .data(userService.getUserById(userId))
                .build();
    }

    @GetMapping("/myInfo")
    public ApiResponse<?> getMyInfo(){
        return ApiResponse.builder()
                .message("get my info successfully")
                .data(userService.getMyInfo())
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<?> getALlUsers(@RequestParam(defaultValue = "0") int pageNo,
                                      @RequestParam(defaultValue = "10") int pageSize, String... sorts){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(authority -> log.info(authority.getAuthority()));

        return ApiResponse.builder()
                .message("get all users simple successfully")
                .data(userService.getAllUsers(pageNo, pageSize, sorts))
                .build();
    }


    @DeleteMapping("/{userId}")
    public ApiResponse<?> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return ApiResponse.builder()
                .message("User deleted successfully")
                .build();
    }

    @PostMapping("active/{userId}")
    public ApiResponse<?> changeActive(@PathVariable String userId, @RequestParam boolean active){

        String message = active ? "activated" : "deactivated";
        return ApiResponse.builder()
                .message("Change " + message + " successfully")
                .data(userService.changeActive(userId, active))
                .build();
    }
    @PostMapping("/addTutor")
    public ApiResponse<?> addTutor(@RequestBody @Valid TutorRequest request){

        return ApiResponse.builder()
                .success(true)
                .data(userService.addTutor(request))
                .message("Tutor added and verification request sent successfully")
                .build();
    }
    @PutMapping("/updateTutorInfo")
    public ApiResponse<?> updateTutorInfo(@RequestBody @Valid TutorInfoRequest request){

        return ApiResponse.builder()
                .success(true)
                .data(userService.updateTutorInfo(request))
                .message("Tutor info updated successfully")
                .build();
    }

    @GetMapping("/requests")
    public ApiResponse<?> getAllRequests(@RequestParam(defaultValue = "0") int pageNo,
                                         @RequestParam(defaultValue = "10") int pageSize, String... sorts){
        return ApiResponse.builder()
                .success(true)
                .data(userService.getRequests(pageNo, pageSize, sorts))
                .message("Get all requests successfully")
                .build();
    }

    @PostMapping("/approve")
    public ApiResponse<?> approveRequest(@RequestBody ApproveRequest request){
        return ApiResponse.builder()
                .success(true)
                .data(userService.approveRequest(request))
                .message("Approve request successfully")
                .build();
    }
    @PostMapping("/reject")
    public ApiResponse<?> rejectRequest(@RequestBody RejectRequest request){
        return ApiResponse.builder()
                .success(true)
                .data(userService.rejectRequest(request))
                .message("Reject request successfully")
                .build();
    }

    @PostMapping("/withdraw")
    public ApiResponse<?> withdrawMoney(@RequestParam double withdrawAmount){
        return ApiResponse.builder()
                .success(true)
                .data(userService.withdrawMoney(withdrawAmount))
                .message("Withdraw request created successfully")
                .build();
    }

}