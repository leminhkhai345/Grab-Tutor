package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.CourseRequest;
import com.grabtutor.grabtutor.dto.response.CourseResponse;

import java.util.List;
import java.util.Set;

public interface CourseService {
    CourseResponse createCourse(CourseRequest request, String tutorId, Set<String> subjectIds);
    CourseResponse getCourseById(String courseId);
    CourseResponse updateCourse(String tutorId, String courseId, CourseRequest request);
    void deleteCourse(String courseId);
    List<CourseResponse> getAllCoursesByTutorId(String tutorId, int pageNo, int pageSize, String... sorts);
}
