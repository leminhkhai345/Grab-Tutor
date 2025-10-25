package com.grabtutor.grabtutor.dto.response;


import com.grabtutor.grabtutor.entity.Lesson;
import com.grabtutor.grabtutor.entity.Subject;
import com.grabtutor.grabtutor.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}
