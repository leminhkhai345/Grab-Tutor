package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.request.CourseRequest;
import com.grabtutor.grabtutor.dto.response.CourseResponse;
import com.grabtutor.grabtutor.entity.BaseEntity;
import com.grabtutor.grabtutor.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    Course toCourse(CourseRequest request);
    default CourseResponse toCourseResponse(Course course){
        if(course == null) {
            return null;
        }
        return CourseResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .price(course.getPrice())
                .imageUrl(course.getImageUrl())
                .isPublished(course.isPublished())
                .totalLessons(course.getLessons() != null ? course.getLessons().size() : 0)
                .subjectIds(course.getSubjects() != null ? course.getSubjects().stream().map(BaseEntity::getId).collect(Collectors.toSet()) : null)
                .tutorId(course.getTutor() != null ? course.getTutor().getId() : null)
                .lessonIds(course.getLessons() != null ? course.getLessons().stream().map(BaseEntity::getId).toList() : null)
                .build();
    }
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
