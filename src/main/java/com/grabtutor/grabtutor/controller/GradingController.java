package com.grabtutor.grabtutor.controller;


import com.grabtutor.grabtutor.service.GeminiGradingService;
import lombok.RequiredArgsConstructor;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController

@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/grading")
public class GradingController {

    private GeminiGradingService geminiService;

    @PostMapping("/check")
    public ResponseEntity<?> checkHomework(
            @RequestParam("postId") String postId,
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Vui lòng tải lên file bài làm");
        }

        String result = geminiService.checkHomework(postId, file);
        return ResponseEntity.ok(result);
    }
}