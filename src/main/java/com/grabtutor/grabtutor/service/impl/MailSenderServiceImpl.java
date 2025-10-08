package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.OTPVerificationRequest;
import com.grabtutor.grabtutor.dto.request.SendOTPRequest;
import com.grabtutor.grabtutor.entity.Otp;
import com.grabtutor.grabtutor.enums.OtpType;
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
    public void sendOtp(SendOTPRequest request, OtpType otpType) {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        SimpleMailMessage message = new SimpleMailMessage();
        if(otpType == OtpType.REGISTER && !Objects.isNull(userRepository.findByEmail(request.getEmail()))) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        var otp = otpRepository.findOtpByEmail(request.getEmail());
        if(Objects.isNull(otp)){
            var newOtp = Otp.builder()
                        .code(String.valueOf(code))
                        .expiryTime(LocalDateTime.now().plusSeconds(300))
                        .build();
            otpRepository.save(newOtp);
        } else{
            otp.setCode(String.valueOf(code));
            otp.setExpiryTime(LocalDateTime.now().plusSeconds(300));
            otp.setUsed(false);
            otpRepository.save(otp);
        }
        message.setTo(request.getEmail());
        message.setSubject("OTP verification");
        message.setText("Here is your OTP verification code: " + code);
        mailSender.send(message);
    }

    @Override
    public void verifyOtp(OTPVerificationRequest request) {
        var otp = otpRepository.findOtpByEmail((request.getCode()));
        if(Objects.isNull(otp)){
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }
        if (!otp.getCode().equals(request.getCode())) {
            throw new AppException(ErrorCode.OTP_INVALID);
        }
        if(otp.isUsed()){
            throw new AppException(ErrorCode.OTP_USED);
        }
        if(!otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }
    }
}
