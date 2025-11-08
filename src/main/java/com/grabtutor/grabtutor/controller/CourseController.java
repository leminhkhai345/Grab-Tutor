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
import org.springframework.security.core.parameters.P;
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
            @RequestParam("subjectIds") Set<String> subjectIds) throws Exception {
        CourseRequest request = objectMapper.readValue(courseJson, CourseRequest.class);
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = fileUploadService.uploadFile(imageFile);
            request.setImageUrl(imageUrl);
        }
        return ApiResponse.builder()
                .message("Course created successfully")
                .data(courseService.createCourse(request, subjectIds))
                .build();
    }

    @PutMapping("/{courseId}")
    public ApiResponse<?> updateCourse(
            @PathVariable("courseId") String courseId,
            @RequestParam("course") String courseJson,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam("subjectIds") Set<String> subjectIds) throws Exception {
        CourseRequest request = objectMapper.readValue(courseJson, CourseRequest.class);
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = fileUploadService.uploadFile(imageFile);
            request.setImageUrl(imageUrl);
        }
        return ApiResponse.builder()
                .message("Course updated successfully")
                .data(courseService.updateCourse(courseId, request, subjectIds))
                .build();
    }

    @GetMapping("/{courseId}")
    public ApiResponse<?> getCourseByCourseId(@PathVariable String courseId) {
        return ApiResponse.builder()
                .message("Course fetched successfully")
                .data(courseService.getCourseByCourseId(courseId))
                .build();
    }

    @DeleteMapping("/{courseId}")
    public ApiResponse<?> deleteCourse(@PathVariable String courseId) {
        courseService.deleteCourse(courseId);
        return ApiResponse.builder()
                .message("Course deleted successfully")
                .build();
    }

    @PutMapping("/publish/{courseId}")
    public ApiResponse<?> changePublishCourse(
            @PathVariable String courseId,
            @RequestParam boolean isPublished) {
        return ApiResponse.builder()
                .message("Course publish status changed successfully")
                .data(courseService.changePublishCourse(courseId, isPublished))
                .build();
    }

    @GetMapping("/tutor/{tutorId}")
    public ApiResponse<?> getAllCoursesByTutorId(
            @PathVariable String tutorId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String... sorts) {
        return ApiResponse.builder()
                .message("Courses fetched successfully")
                .data(courseService.getAllCoursesByTutorId(tutorId, pageNo, pageSize, sorts))
                .build();
    }

    @PostMapping("/enroll/{courseId}")
    public ApiResponse<?> enrollCourse(@PathVariable String courseId) {
        return ApiResponse.builder()
                .message("Course enrolled successfully")
                .data(courseService.enrollCourse(courseId))
                .build();
    }

    @GetMapping("/myEnrolledCourses")
    public ApiResponse<?> getMyEnrolledCourses(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String... sorts) {
        return ApiResponse.builder()
                .message("Enrolled courses fetched successfully")
                .data(courseService.getMyEnrolledCourses(pageNo, pageSize, sorts))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<?> searchCourse(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String... sorts) {
        return ApiResponse.builder()
                .message("Courses fetched successfully")
                .data(courseService.searchCourse(keyword, pageNo, pageSize, sorts))
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<?> getAllCourse(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String... sorts) {
        return ApiResponse.builder()
                .message("Courses fetched successfully")
                .data(courseService.getAllCourse(pageNo, pageSize, sorts))
                .build();
    }
}
