package com.grabtutor.grabtutor.controller;

import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.service.StatisticService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
