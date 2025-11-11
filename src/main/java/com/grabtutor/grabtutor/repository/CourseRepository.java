package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.Course;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, String> {
    @EntityGraph(attributePaths = {"subjects"})
    Page<Course>  findByTutorIdAndIsDeletedFalse(String tutorId, Pageable pageable);
    @Query("SELECT c FROM Course c WHERE c.isDeleted = false AND c.isPublished = true")
    Page<Course>  findAllCourses(Pageable pageable);
    @EntityGraph(attributePaths = {"tutor"})
    Optional<Course> findById(String id);

    Page<Course>  findByEnrolledUsersIdAndIsDeletedFalse(String userId, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE c.isDeleted = false AND c.name LIKE CONCAT('%', :keyword, '%')")
    Page<Course> searchCoursesByName(@Param("keyword") String keyword, Pageable pageable);
    Page<Course> findBySubjects_Id(String subjectId, Pageable pageable);

}
