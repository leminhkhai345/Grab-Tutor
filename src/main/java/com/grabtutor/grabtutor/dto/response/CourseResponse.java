package com.grabtutor.grabtutor.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseResponse {
    String id;
    String name;
    String description;
    double price;
    String imageUrl;
    boolean isPublished;
    int totalLessons;
    List<String> subjectIds;
    String tutorId;
    List<String> lessonIds;
    boolean isEnrolled;
}
