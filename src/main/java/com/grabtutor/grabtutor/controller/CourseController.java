package com.grabtutor.grabtutor.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.grabtutor.grabtutor.dto.request.CourseRequest;
import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.service.CourseService;
import com.grabtutor.grabtutor.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CourseController {
    CourseService courseService;
    FileUploadService fileUploadService;
    ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> createCourse(
            @RequestParam("course") String courseJson,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam("subjectIds") Set<String> subjectIds,
            @AuthenticationPrincipal Jwt jwt) throws Exception {
        String tutorId = jwt.getClaimAsString("userId");
        CourseRequest request = objectMapper.readValue(courseJson, CourseRequest.class);
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = fileUploadService.uploadFile(imageFile);
            request.setImageUrl(imageUrl);
        }
        return ApiResponse.builder()
                .message("Course created successfully")
                .data(courseService.createCourse(request, tutorId, subjectIds))
                .build();
    }

    @PutMapping("/{courseId}")
    public ApiResponse<?> updateCourse(
            @PathVariable("courseId") String courseId,
            @RequestParam("course") String courseJson,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam("subjectIds") Set<String> subjectIds,
            @AuthenticationPrincipal Jwt jwt) throws Exception {
        String tutorId = jwt.getClaimAsString("userId");
        CourseRequest request = objectMapper.readValue(courseJson, CourseRequest.class);
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = fileUploadService.uploadFile(imageFile);
            request.setImageUrl(imageUrl);
        }
        return ApiResponse.builder()
                .message("Course updated successfully")
                .data(courseService.updateCourse(tutorId, courseId, request, subjectIds))
                .build();
    }

    @GetMapping("/{courseId}")
    public ApiResponse<?> getCourseById(@PathVariable String courseId, @AuthenticationPrincipal Jwt jwt) {
        String tutorId = jwt.getClaimAsString("userId");
        return ApiResponse.builder()
                .message("Course fetched successfully")
                .data(courseService.getCourseById(courseId, tutorId))
                .build();
    }

    @DeleteMapping("/{courseId}")
    public ApiResponse<?> deleteCourse(@PathVariable String courseId) {
        courseService.deleteCourse(courseId);
        return ApiResponse.builder()
                .message("Course deleted successfully")
                .build();
    }

}
