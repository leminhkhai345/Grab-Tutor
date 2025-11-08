package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.LessonRequest;
import com.grabtutor.grabtutor.dto.response.LessonResponse;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.entity.Course;
import com.grabtutor.grabtutor.entity.Lesson;
import com.grabtutor.grabtutor.entity.User;
import com.grabtutor.grabtutor.enums.Role;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.LessonMapper;
import com.grabtutor.grabtutor.repository.CourseRepository;
import com.grabtutor.grabtutor.repository.LessonRepository;
import com.grabtutor.grabtutor.repository.UserRepository;
import com.grabtutor.grabtutor.service.LessonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('TUTOR')")
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
        course.setTotalLessons(course.getTotalLessons() + 1);
        courseRepository.save(course);
        return lessonMapper.toLessonResponse(lessonRepository.save(lesson));
    }
    @PreAuthorize("hasRole('TUTOR')")
    @Transactional
    @Override
    public LessonResponse updateLesson(String lessonId, LessonRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String tutorId = jwt.getClaimAsString("userId");
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));
        if(lesson.isDeleted()) {
            throw new AppException(ErrorCode.LESSON_NOT_FOUND);
        }
        if (!lesson.getCourse().getTutor().getId().equals(tutorId)) {
            throw new AppException(ErrorCode.TUTOR_NOT_AUTHORIZED);
        }
        lessonMapper.updateLessonFromRequest(request, lesson);
        return lessonMapper.toLessonResponse(lessonRepository.save(lesson));
    }

    @Override
    public LessonResponse getLessonByLessonId(String lessonId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));
        if(lesson.isDeleted()) {
            throw new AppException(ErrorCode.LESSON_NOT_FOUND);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        boolean isAccessible = false;
        if(lesson.isPreview()){
            isAccessible = true;
        }

        else if(user.getRole() == Role.ADMIN){
            isAccessible = true;
        }
        else if(user.getRole() == Role.TUTOR){
            if(lesson.getCourse().getTutor().getId().equals(userId)){
                isAccessible = true;
            }
        }
        else if(user.getRole() == Role.USER){
            if(lesson.getCourse().getEnrolledUsers().contains(user)){
                isAccessible = true;
            }
        }


        if(!isAccessible){
            throw new AppException(ErrorCode.LESSON_NOT_ACCESSIBLE);
        }
        return lessonMapper.toLessonResponse(lesson);
    }

    @Override
    public PageResponse<?> getAllLessonsByCourseId(String courseId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("lessonNumber").descending());
        Page<Lesson> lessons = lessonRepository.findAllByCourseIdAndIsDeletedFalse(courseId, pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(lessons.getTotalPages())
                .items(lessons.stream().map(lessonMapper::toLessonResponse).toList())
                .build();
    }


    @PreAuthorize("hasRole('TUTOR')")
    @Transactional
    @Override
    public void deleteLesson(String lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String tutorId = jwt.getClaimAsString("userId");

        if (!lesson.getCourse().getTutor().getId().equals(tutorId)) {
            throw new AppException(ErrorCode.TUTOR_NOT_AUTHORIZED);
        }
        lesson.setDeleted(true);
        Course course = lesson.getCourse();
        course.setTotalLessons(course.getTotalLessons() - 1);
        courseRepository.save(course);
        lessonRepository.save(lesson);
    }
}
