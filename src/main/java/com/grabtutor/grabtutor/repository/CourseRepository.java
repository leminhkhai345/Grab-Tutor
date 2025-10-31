package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, String> {
    Page<Course>  findByTutorIdAndIsDeletedFalse(String tutorId, Pageable pageable);

    @EntityGraph(attributePaths = {"tutor"})
    Optional<Course> findById(String id);
    Page<Course>  findByEnrolledUsersIdAndIsDeletedFalse(String userId, Pageable pageable);

}
