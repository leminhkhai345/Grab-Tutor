package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.LessonRequest;
import com.grabtutor.grabtutor.dto.response.LessonResponse;
import com.grabtutor.grabtutor.dto.response.PageResponse;

public interface LessonService {
    LessonResponse createLesson(String courseId, LessonRequest request);
    LessonResponse updateLesson(String lessonId, LessonRequest request);
    LessonResponse getLessonByLessonId(String lessonId);
    void deleteLesson(String lessonId);
    PageResponse<?> getAllLessonsByCourseId(String courseId, int pageNo, int pageSize);
}
