package com.grabtutor.grabtutor.controller;

import com.grabtutor.grabtutor.dto.request.LoadMessagesRequest;
import com.grabtutor.grabtutor.dto.request.MessageRequest;
import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {
    MessageService messageService;
    @GetMapping("/")
    public ApiResponse<?> loadMessages(LoadMessagesRequest request) {
        return ApiResponse.builder()
                .data(messageService.loadMessages(request))
                .message("Load message successfully")
                .build();
    }
}
