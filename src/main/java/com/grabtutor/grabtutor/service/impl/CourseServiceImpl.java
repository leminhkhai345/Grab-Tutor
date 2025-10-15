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
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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
    public CourseResponse createCourse(CourseRequest request, Set<String> subjectIds) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String tutorId = jwt.getClaimAsString("userId");
        User tutor = userRepository.findById(tutorId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Course course = courseMapper.toCourse(request);
        if(subjectIds != null && !subjectIds.isEmpty()) {
            Set<Subject> subjects = new HashSet<>(subjectRepository.findAllById(subjectIds));
            course.setSubjects(subjects);
        }
        course.setTutor(tutor);
        return courseMapper.toCourseResponse(courseRepository.save(course));
    }


    @Override
    public CourseResponse updateCourse(String courseId, CourseRequest request,
                                       Set<String> subjectIds) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String tutorId = jwt.getClaimAsString("userId");
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
    public CourseResponse getCourseById(String courseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String tutorId = jwt.getClaimAsString("userId");

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String tutorId = jwt.getClaimAsString("userId");
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        if(course.isDeleted()) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }
        if(!course.getTutor().getId().equals(tutorId)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        course.setDeleted(true);
        courseRepository.save(course);
    }

    @Override
    public List<CourseResponse> getAllCoursesByTutorId(String tutorId, int pageNo, int pageSize, String... sorts) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sorts));
        Page<Course> courses = courseRepository.findByTutorIdAndIsDeletedFalse(tutorId, pageable);
        return courses.stream()
                .map(courseMapper::toCourseResponse)
                .toList();
    }
}
