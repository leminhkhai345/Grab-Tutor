package com.grabtutor.grabtutor.controller;

import com.grabtutor.grabtutor.dto.request.*;
import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.dto.response.AuthenticationResponse;
import com.grabtutor.grabtutor.dto.response.IntrospectResponse;
import com.grabtutor.grabtutor.enums.OtpType;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.service.impl.AuthenticationServiceImpl;
import com.grabtutor.grabtutor.service.impl.MailSenderServiceImpl;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationServiceImpl authenticationService;
    MailSenderServiceImpl sendMailService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder().data(result).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().data(result).build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder().data(result).build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/change-password")
    ApiResponse<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
          String userId = authenticationService.getUserIdFromSecurityContext();
            if (userId == null) {
                throw new AppException(ErrorCode.TOKEN_INVALID);
            }
            authenticationService.changePassword(userId, request);
        return ApiResponse.builder()
                .message("Change password successfully")
                .build();
    }
    @PostMapping("/change-forgot-password")
    ApiResponse<?> changeForgotPassword(@Valid @RequestBody ChangeForgotPasswordRequest request) {
        authenticationService.changeForgotPassword(request);
        return ApiResponse.builder()
                .message("Change password successfully")
                .build();
    }
    @PostMapping("/send-register-otp")
    public ApiResponse<?> sendRegisterOtp(@RequestBody SendOTPRequest request){
        sendMailService.sendOtp(request, OtpType.REGISTER);
        return ApiResponse.builder()
                .success(true)
                .message("OTP sending successfully")
                .build();
    }
    @PostMapping("/send-forgot-password-otp")
    public ApiResponse<?> sendForgotPasswordOtp(@RequestBody SendOTPRequest request){
        sendMailService.sendOtp(request, OtpType.FORGOT_PASSWORD);
        return ApiResponse.builder()
                .success(true)
                .message("OTP sending successfully")
                .build();
    }
    @PostMapping("/verify-otp")
    public ApiResponse<?> verifyOtp(@RequestBody OTPVerificationRequest request){
        sendMailService.verifyOtp(request);
        return ApiResponse.builder()
                .success(true)
                .message("OTP verified successfully")
                .build();
    }
}
