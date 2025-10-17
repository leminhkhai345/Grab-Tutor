package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.OTPVerificationRequest;
import com.grabtutor.grabtutor.dto.request.SendOTPRequest;
import com.grabtutor.grabtutor.enums.OtpType;

public interface MailSenderService {
    void sendOtp(SendOTPRequest request, OtpType otpType);
    void verifyOtp(OTPVerificationRequest request);
}
