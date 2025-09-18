package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.OTPVerificationRequest;
import com.grabtutor.grabtutor.dto.request.SendOTPRequest;

public interface MailSenderService {
    void sendMail(String to, String subject, String body);
    void sendOTP(SendOTPRequest request);
    void verifyOTP(OTPVerificationRequest request);
}
