package com.grabtutor.grabtutor.controller;

import com.grabtutor.grabtutor.dto.request.*;
import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.service.MailSenderService;
import com.grabtutor.grabtutor.service.impl.MailSenderServiceImpl;
import com.grabtutor.grabtutor.service.impl.UserServiceImpl;

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
    UserServiceImpl userService;
    MailSenderServiceImpl sendMailService;
    @PostMapping
    public ApiResponse<?> createUser(@RequestBody @Valid UserRequest userRequest){
        return ApiResponse.builder()
                .message("User created successfully")
                .data(userService.addUser(userRequest))
                .build();
    }

    @PostMapping("/{id}")
    public ApiResponse<?> updateUser(@PathVariable String id, @RequestBody @Valid UserRequest userRequest){
        return ApiResponse.builder()
                .message("User updated successfully")
                .data(userService.updateUser(id, userRequest))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getUserById(@PathVariable String id){
        return ApiResponse.builder()
                .message("get user by id successfully")
                .data(userService.getUserById(id))
                .build();
    }

    @GetMapping("/myInfo")
    public ApiResponse<?> getMyInfo(){
        return ApiResponse.builder()
                .message("get my info successfully")
                .data(userService.getMyInfo())
                .build();
    }

    @GetMapping
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


    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return ApiResponse.builder()
                .message("User deleted successfully")
                .build();
    }

    @PostMapping("active/{id}")
    public ApiResponse<?> changeActive(@PathVariable String id, @RequestParam boolean active){

        String message = active ? "activated" : "deactivated";
        return ApiResponse.builder()
                .message("Change " + message + " successfully")
                .data(userService.changeActive(id, active))
                .build();
    }
    //Test gửi mail
    @PostMapping("/sendMail")
    public ApiResponse<?> sendMail(@RequestBody @Valid SendMailRequest request){
        sendMailService.sendMail(request.getTo(), request.getSubject(), request.getBody());
        return ApiResponse.builder()
                .message("Mail sent successfully")
                .build();
    }
    @PostMapping("/addInfo")
    public ApiResponse<?> addInfo(@RequestBody @Valid TutorInfoRequest request){
        return ApiResponse.builder()
                .success(true)
                .data(userService.addInfo(request))
                .message("Tutor info added successfully")
                .build();
    }
    @PostMapping("/submitRequest")
    public ApiResponse<?> submitVerificationRequest(AccountVerificationRequest request){
        return ApiResponse.builder()
                .success(true)
                .data(userService.submitRequest(request))
                .message("Sending request successfully")
                .build();
    }
    @PostMapping("/request")
    public ApiResponse<?> approveRequest(ApproveRequest request){
        return ApiResponse.builder()
                .success(true)
//                .data(userService.approveRequest(request))
                .message("Approve request successfully")
                .build();
    }
    @PostMapping("/request")
    public ApiResponse<?> rejectRequest(RejectRequest request){
        return ApiResponse.builder()
                .success(true)
//                .data(userService.rejectRequest(request))
                .message("Reject request successfully")
                .build();
    }
    @PostMapping("/sendOtp")
    public ApiResponse<?> sendOTP(SendOTPRequest request){
        sendMailService.sendOTP(request);
        return ApiResponse.builder()
                .success(true)
                .message("OTP sending successfully")
                .build();
    }
    @PostMapping("/verifyOtp")
    public ApiResponse<?> verifyOTP(OTPVerificationRequest request){
        sendMailService.verifyOTP(request);
        return ApiResponse.builder()
                .success(true)
                .message("OTP verified successfully")
                .build();
    }
}