package com.grabtutor.grabtutor.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "lessons")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Lesson extends BaseEntity {
    int lessonNumber;
    String title;
    String content;
    String videoUrl;
    String imageUrl; // anh dai dien bai hoc
    @Builder.Default
    boolean isPublished = true;
    boolean isPreview;


    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    Course course;
}
