package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, String> {
    @EntityGraph(attributePaths = {"course.tutor"})
    Optional<Lesson> findById(String id);
    Page<Lesson> findAllByCourseIdAndIsDeletedFalse(String courseId, Pageable pageable);
}
