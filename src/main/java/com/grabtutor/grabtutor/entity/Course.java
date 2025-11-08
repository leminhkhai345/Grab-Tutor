package com.grabtutor.grabtutor.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "courses")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@EqualsAndHashCode(callSuper = true, exclude = {"subjects", "lessons", "enrolledUsers", "transactions"})
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Course extends BaseEntity {
    String name;
    String description;
    double price;
    String imageUrl; // anh dai dien khoa hoc
    @Builder.Default
    boolean isPublished = true;
    @Builder.Default
    int totalLessons = 0;
    @ManyToMany(fetch = FetchType.EAGER)
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
    @Builder.Default
    List<Lesson> lessons = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_course",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonBackReference
    Set<User> enrolledUsers;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    List<UserTransaction> transactions = new ArrayList<>();


}
