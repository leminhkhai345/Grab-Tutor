package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.CourseRequest;
import com.grabtutor.grabtutor.dto.response.CourseResponse;
import com.grabtutor.grabtutor.entity.Course;
import com.grabtutor.grabtutor.entity.Subject;
import com.grabtutor.grabtutor.entity.User;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.CourseMapper;
import com.grabtutor.grabtutor.repository.CourseRepository;
import com.grabtutor.grabtutor.repository.SubjectRepository;
import com.grabtutor.grabtutor.repository.UserRepository;
import com.grabtutor.grabtutor.service.CourseService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CourseServiceImpl implements CourseService {
    CourseRepository courseRepository;
    CourseMapper courseMapper;
    UserRepository userRepository;
    SubjectRepository subjectRepository;


    @Override
    public CourseResponse createCourse(CourseRequest request, String tutorId, Set<String> subjectIds) {
        User tutor = userRepository.findById(tutorId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Course course = courseMapper.toCourse(request);
        Set<Subject> subjects = new HashSet<>(subjectRepository.findAllById(subjectIds));
        course.setSubjects(subjects);
        course.setTutor(tutor);
        return courseMapper.toCourseResponse(courseRepository.save(course));
    }


    @Override
    public CourseResponse updateCourse(String tutorId, String courseId, CourseRequest request,
                                       Set<String> subjectIds) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        if(course.isDeleted()) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }
        if(!course.getTutor().getId().equals(tutorId)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        courseMapper.updateCourseFromRequest(request, course);
        if(!subjectIds.isEmpty()){
            Set<Subject> subjects = new HashSet<>(subjectRepository.findAllById(subjectIds));
            course.setSubjects(subjects);
        }
        return courseMapper.toCourseResponse(courseRepository.save(course));
    }

    @Override
    public CourseResponse getCourseById(String courseId, String tutorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        if(course.isDeleted()) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }
        if(!course.getTutor().getId().equals(tutorId) && !course.isPublished()) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        return courseMapper.toCourseResponse(course);
    }

    @Override
    public void deleteCourse(String courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        if(course.isDeleted()) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }
        course.setDeleted(true);
        courseRepository.save(course);
    }

    @Override
    public List<CourseResponse> getAllCoursesByTutorId(String tutorId, int pageNo, int pageSize, String... sorts) {
        return List.of();
    }
}
