package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.request.LessonRequest;
import com.grabtutor.grabtutor.dto.response.LessonResponse;
import com.grabtutor.grabtutor.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    default Lesson toLesson(LessonRequest lessonRequest) {
        if ( lessonRequest == null ) {
            return null;
        }

        Lesson.LessonBuilder<?, ?> lesson = Lesson.builder();

        if ( lessonRequest.getLessonNumber() != null ) {
            lesson.lessonNumber( lessonRequest.getLessonNumber() );
        }
        lesson.title( lessonRequest.getTitle() );
        lesson.content( lessonRequest.getContent() );
        lesson.videoUrl( lessonRequest.getVideoUrl() );
        lesson.imageUrl( lessonRequest.getImageUrl() );
        lesson.isPreview(lessonRequest.isPreview());

        return lesson.build();
    }
    default LessonResponse toLessonResponse(Lesson lesson){
        return LessonResponse.builder()
                .id(lesson.getId())
                .lessonNumber(lesson.getLessonNumber())
                .title(lesson.getTitle())
                .content(lesson.getContent())
                .videoUrl(lesson.getVideoUrl())
                .imageUrl(lesson.getImageUrl())
                .isPublished(lesson.isPublished())
                .isPreview(lesson.isPreview())
                .courseId(lesson.getCourse().getId())
                .build();
    }
    default void updateLessonFromRequest(LessonRequest lessonRequest, @MappingTarget Lesson lesson){
        if (lessonRequest.getLessonNumber() != null) {
            lesson.setLessonNumber(lessonRequest.getLessonNumber());
        }
        if (lessonRequest.getTitle() != null) {
            lesson.setTitle(lessonRequest.getTitle());
        }
        if (lessonRequest.getContent() != null) {
            lesson.setContent(lessonRequest.getContent());
        }
        if (lessonRequest.getVideoUrl() != null && !lessonRequest.getVideoUrl().isEmpty()) {
            lesson.setVideoUrl(lessonRequest.getVideoUrl());
        }
        if (lessonRequest.getImageUrl() != null && !lessonRequest.getImageUrl().isEmpty()) {
            lesson.setImageUrl(lessonRequest.getImageUrl());
        }
        if(lessonRequest.isPreview() != lesson.isPreview()) {
            lesson.setPreview(lessonRequest.isPreview());
        }
    }
}
