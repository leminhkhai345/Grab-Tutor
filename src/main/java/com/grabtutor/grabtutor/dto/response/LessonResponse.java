package com.grabtutor.grabtutor.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonResponse {
    String id;
    int lessonNumber;
    String title;
    String content;
    String videoUrl;
    String imageUrl;
    boolean isPublished;
    boolean isPreview;
    String courseId;
}
