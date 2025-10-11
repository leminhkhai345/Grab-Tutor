package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, String> {
}
