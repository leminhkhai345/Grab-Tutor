package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.OTPVerificationRequest;
import com.grabtutor.grabtutor.dto.request.SendOTPRequest;
import com.grabtutor.grabtutor.entity.Otp;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.repository.OtpRepository;
import com.grabtutor.grabtutor.repository.UserRepository;
import com.grabtutor.grabtutor.service.MailSenderService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.apache.catalina.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Objects;
import java.util.Random;
import java.util.random.RandomGenerator;

@Service
@Builder
@Data
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class MailSenderServiceImpl implements MailSenderService {
    JavaMailSender mailSender;
    UserRepository userRepository;
    OtpRepository otpRepository;
    @Override
    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    @Override
    public void sendOTP(SendOTPRequest request) {
        var user = userRepository.findById(request.getUserId()).orElseThrow(() ->new AppException(ErrorCode.USER_NOT_FOUND));

        Random random = new Random();
        int code = 100000 + random.nextInt(900000);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("OTP verification");
        message.setText("Here is your OTP verification code: " + code);
        mailSender.send(message);

        var otp = Otp.builder()
                .code(String.valueOf(code))
                .expiryTime(LocalDateTime.now().plusSeconds(300))
                .build();
    }

    @Override
    public void verifyOTP(OTPVerificationRequest request) {
        var otps = otpRepository.findOtpByCode(request.getOtp());
        otps.forEach(otp -> {
            if(Objects.equals(otp.getUser().getId(), request.getUserId())){
                if(otp.getExpiryTime().isBefore(LocalDateTime.now())){ throw new AppException(ErrorCode.OTP_EXPIRED); }
                return;
            }
        });
        throw new AppException(ErrorCode.OTP_NOT_FOUND);
    }
}
