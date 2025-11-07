package com.grabtutor.grabtutor.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
public class LessonRequest {
    Integer lessonNumber;
    String title;
    String content;
    String videoUrl;
    String imageUrl;
    boolean isPreview;
}
