package com.grabtutor.grabtutor.service;

public interface MailSenderService {
    void sendMail(String to, String subject, String body);
}
