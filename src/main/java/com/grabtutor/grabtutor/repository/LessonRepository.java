package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, String> {
}
