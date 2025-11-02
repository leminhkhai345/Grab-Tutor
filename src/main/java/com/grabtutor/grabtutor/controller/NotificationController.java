package com.grabtutor.grabtutor.controller;

import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.websocket.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    NotificationService notificationService;
    @GetMapping
    public ApiResponse<?> sendNotification(@RequestParam String userId,
                                           @RequestParam(defaultValue = "0") int pageNo,
                                           @RequestParam(defaultValue = "10") int pageSize,
                                           @RequestParam String... sorts)
    {
        return ApiResponse.builder()
                .data(notificationService.getNotificationByUserId(userId, pageNo, pageSize, sorts))
                .message("Get notification successfully!")
                .build();
    }

}
