package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.request.CourseRequest;
import com.grabtutor.grabtutor.dto.response.CourseResponse;
import com.grabtutor.grabtutor.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    Course toCourse(CourseRequest request);
    CourseResponse toCourseResponse(Course course);
    void updateCourseFromRequest(CourseRequest request, @MappingTarget Course course);
}
