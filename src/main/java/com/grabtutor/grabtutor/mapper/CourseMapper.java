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
    default void updateCourseFromRequest(CourseRequest request, @MappingTarget Course course){
        if(request.getName() != null) {
            course.setName(request.getName());
        }
        if(request.getDescription() != null) {
            course.setDescription(request.getDescription());
        }
        if(request.getPrice() > 0) {
            course.setPrice(request.getPrice());
        }
        if(request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            course.setImageUrl(request.getImageUrl());
        }
    }
}
