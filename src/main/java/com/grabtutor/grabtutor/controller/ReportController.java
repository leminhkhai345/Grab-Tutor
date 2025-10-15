package com.grabtutor.grabtutor.controller;

import com.grabtutor.grabtutor.dto.request.ReportRequest;
import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {
    ReportService reportService;

    @PostMapping("/post/{postId}/")
    public ApiResponse<?> createReport(@PathVariable String postId,
                                       @RequestBody @Valid ReportRequest request){
        return  ApiResponse.builder()
                .message("Report created successfully")
                .data(reportService.createReport(request, postId))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getReportById(@PathVariable String id){
        return  ApiResponse.builder()
                .message("Report fetched successfully")
                .data(reportService.getReportById(id))
                .build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<?> getReportByUserId(@PathVariable String userId,
                                            @RequestParam(defaultValue = "0") int pageNo,
                                            @RequestParam(defaultValue = "10") int pageSize,
                                            @RequestParam(defaultValue = "createdAt:desc") String... sortBy){
        return  ApiResponse.builder()
                .message("Reports fetched successfully")
                .data(reportService.getReportByUserId(userId, pageNo, pageSize, sortBy))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteReport(@PathVariable String id){
        reportService.deleteReport(id);
        return  ApiResponse.builder()
                .message("Report deleted successfully")
                .build();
    }

}
