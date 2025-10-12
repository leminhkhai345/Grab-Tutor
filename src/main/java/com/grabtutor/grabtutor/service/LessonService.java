package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.LessonRequest;
import com.grabtutor.grabtutor.dto.response.LessonResponse;

public interface LessonService {
    LessonResponse createLesson(String tutorId, String courseId, LessonRequest request);
    LessonResponse updateLesson(String tutorId, String lessonId, LessonRequest request);
    LessonResponse getLessonByLessonId(String lessonId);
    void deleteLesson(String lessonId);
}
