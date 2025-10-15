package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.LessonRequest;
import com.grabtutor.grabtutor.dto.response.LessonResponse;
import com.grabtutor.grabtutor.entity.Course;
import com.grabtutor.grabtutor.entity.Lesson;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.LessonMapper;
import com.grabtutor.grabtutor.repository.CourseRepository;
import com.grabtutor.grabtutor.repository.LessonRepository;
import com.grabtutor.grabtutor.service.LessonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonServiceImpl implements LessonService {
    LessonRepository lessonRepository;
    LessonMapper lessonMapper;
    CourseRepository courseRepository;

    @Transactional
    @Override
    public LessonResponse createLesson(String courseId, LessonRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String tutorId = jwt.getClaimAsString("userId");
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        if (!course.getTutor().getId().equals(tutorId)) {
            throw new AppException(ErrorCode.TUTOR_NOT_AUTHORIZED);
        }

        Lesson lesson = lessonMapper.toLesson(request);
        lesson.setCourse(course);
        return lessonMapper.toLessonResponse(lessonRepository.save(lesson));
    }
    @Transactional
    @Override
    public LessonResponse updateLesson(String lessonId, LessonRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String tutorId = jwt.getClaimAsString("userId");
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));
        if (!lesson.getCourse().getTutor().getId().equals(tutorId)) {
            throw new AppException(ErrorCode.TUTOR_NOT_AUTHORIZED);
        }
        lessonMapper.updateLessonFromRequest(request, lesson);
        return lessonMapper.toLessonResponse(lessonRepository.save(lesson));
    }

    @Override
    public LessonResponse getLessonByLessonId(String lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));
        return lessonMapper.toLessonResponse(lesson);
    }

    @Transactional
    @Override
    public void deleteLesson(String lessonId) { // Thêm tutorId param
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String tutorId = jwt.getClaimAsString("userId");

        if (!lesson.getCourse().getTutor().getId().equals(tutorId)) {
            throw new AppException(ErrorCode.TUTOR_NOT_AUTHORIZED);
        }

        lesson.setDeleted(true);
        lessonRepository.save(lesson);
    }
}
