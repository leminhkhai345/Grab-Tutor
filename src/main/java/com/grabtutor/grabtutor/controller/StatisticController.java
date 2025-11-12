package com.grabtutor.grabtutor.controller;

import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.enums.Role;
import com.grabtutor.grabtutor.service.StatisticService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticController {
    StatisticService statisticService;

    @GetMapping("/post-status")
    public ApiResponse<?> getPostStatusStatistics(){
        return ApiResponse.builder()
                .data(statisticService.getPostStatusStatistics())
                .message("Post status statistics retrieved successfully")
                .build();
    }

    @GetMapping("/review-stars")
    public ApiResponse<?> getReviewStarStatistics() {
        return ApiResponse.builder()
                .data(statisticService.getReviewStarStatistics())
                .message("Review star statistics retrieved successfully")
                .build();
    }

    @GetMapping("/user-totals")
    public ApiResponse<?> getUserTotalStatistics(){
        return ApiResponse.builder()
                .data(statisticService.getUserTotalStatistics())
                .message("User total statistics retrieved successfully")
                .build();
    }

    @GetMapping("/user-status")
    public ApiResponse<?> getUserStatusStatistics(@RequestParam String role) {
        Role rol;
        try {
            rol = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ApiResponse.builder()
                    .message("Invalid role parameter")
                    .build();
        }
        return ApiResponse.builder()
                .data(statisticService.userStatusStatistics(rol))
                .message("User status statistics retrieved successfully")
                .build();
    }


}
