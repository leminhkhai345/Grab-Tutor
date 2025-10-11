package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.response.LessonResponse;

public interface LessonService {
    LessonResponse createLesson(String tutorId, String courseId, LessonResponse request);
}
