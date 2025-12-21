package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.CourseRequest;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.dto.response.UserTransactionResponse;
import com.grabtutor.grabtutor.dto.response.CourseResponse;

import java.util.Set;

public interface CourseService {
    CourseResponse createCourse(CourseRequest request, Set<String> subjectIds);
    CourseResponse getCourseByCourseId(String courseId);
    CourseResponse updateCourse(String courseId, CourseRequest request, Set<String> subjectIds);
    void deleteCourse(String courseId);
    CourseResponse changePublishCourse(String courseId, boolean isPublished);
    PageResponse<?> getAllCoursesByTutorId(String tutorId, int pageNo, int pageSize, String... sorts);
    UserTransactionResponse enrollCourse(String courseId);
    PageResponse<?> getMyEnrolledCourses(int pageNo, int pageSize, String... sorts);
    PageResponse<?> searchCourse(String keyword, int pageNo, int pageSize, String... sorts);
    PageResponse<?> getAllCourse(int pageNo, int pageSize, String... sorts);
    PageResponse<?> getCoursesBySubjectId(String subjectId, int pageNo, int pageSize, String... sorts);
    int numberOfEnrolledUsers(String courseId);
}
