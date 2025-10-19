package com.grabtutor.grabtutor.controller;

import com.grabtutor.grabtutor.dto.request.LoadMessagesRequest;
import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.service.impl.ChatRoomServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChatRoomController {
    ChatRoomServiceImpl chatRoomService;

    @GetMapping("/messages")
    public ApiResponse<?> loadMessages(LoadMessagesRequest request) {
        return ApiResponse.builder()
                .data(chatRoomService.loadMessages(request))
                .message("Load message successfully")
                .build();
    }
    @GetMapping("/list")
    public ApiResponse<?> loadRooms(){
        return ApiResponse.builder()
                .data(chatRoomService.loadRooms())
                .message("Load rooms successfully")
                .build();
    }
}
