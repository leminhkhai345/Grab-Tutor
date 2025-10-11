package com.grabtutor.grabtutor.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "courses")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class Course extends BaseEntity {
    String name;
    String description;
    double price;
    String imageUrl; // anh dai dien khoa hoc
    @Builder.Default
    boolean isPublished = false;
    @Builder.Default
    int totalLessons = 0;
    @ManyToMany
    @JoinTable(
            name = "course_subject",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    Set<Subject> subjects;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User tutor;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Lesson> lessons;
}
