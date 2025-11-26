package com.grabtutor.grabtutor.service;

import org.springframework.web.multipart.MultipartFile;

public interface GeminiGradingService {
    String checkHomework(String postId, MultipartFile answerFile);
}
