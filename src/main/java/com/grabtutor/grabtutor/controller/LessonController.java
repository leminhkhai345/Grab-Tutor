package com.grabtutor.grabtutor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grabtutor.grabtutor.dto.request.LessonRequest;
import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.service.FileUploadService;
import com.grabtutor.grabtutor.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class LessonController {
    LessonService lessonService;
    ObjectMapper objectMapper;
    FileUploadService fileUploadService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> createLesson(
            @RequestParam("courseId") String courseId,
            @RequestParam("lesson") String lessonJson,
            @RequestParam(value = "video", required = false) MultipartFile video,
            @RequestParam(value = "image", required = false) MultipartFile image) throws Exception {
        LessonRequest request = objectMapper.readValue(lessonJson, LessonRequest.class);
        if (video != null && !video.isEmpty()) {
            request.setVideoUrl(fileUploadService.uploadFile(video));
        }
        if (image != null && !image.isEmpty()) {
            request.setImageUrl(fileUploadService.uploadFile(image));
        }
        return ApiResponse.builder()
                .message("Lesson created successfully")
                .data(lessonService.createLesson(courseId, request))
                .build();
    }

    @PutMapping("/{lessonId}")
    public ApiResponse<?> updateLesson(
            @PathVariable String lessonId,
            @RequestParam("lesson") String lessonJson,
            @RequestParam(value = "video", required = false) MultipartFile video,
            @RequestParam(value = "image", required = false) MultipartFile image) throws Exception {
        LessonRequest request = objectMapper.readValue(lessonJson, LessonRequest.class);
        if (video != null && !video.isEmpty()) {
            request.setVideoUrl(fileUploadService.uploadFile(video));
        }
        if (image != null && !image.isEmpty()) {
            request.setImageUrl(fileUploadService.uploadFile(image));
        }
        return ApiResponse.builder()
                .message("Lesson updated successfully")
                .data(lessonService.updateLesson(lessonId, request))
                .build();
    }

    @GetMapping("/{lessonId}")
    public ApiResponse<?> getLessonById(@PathVariable String lessonId) {
        return ApiResponse.builder()
                .message("Lesson fetched successfully")
                .data(lessonService.getLessonByLessonId(lessonId))
                .build();
    }

    @GetMapping("/course/{courseId}")
    public ApiResponse<?> getAllLessonsByCourseId(@PathVariable String courseId,
                                                  @RequestParam(defaultValue = "0") int pageNo,
                                                  @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.builder()
                .message("Lessons fetched successfully")
                .data(lessonService.getAllLessonsByCourseId(courseId, pageNo, pageSize))
                .build();
    }

    @DeleteMapping("/{lessonId}")
    public ApiResponse<?> deleteLesson(@PathVariable String lessonId) {
        lessonService.deleteLesson(lessonId);
        return ApiResponse.builder()
                .message("Lesson deleted successfully")
                .build();
    }
}
