package com.grabtutor.grabtutor.dto.response;

import com.grabtutor.grabtutor.entity.Course;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonResponse {
    int lessonNumber;
    String title;
    String content;
    String videoUrl;
    String imageUrl;
    boolean isPublished;
    String courseId;
}
