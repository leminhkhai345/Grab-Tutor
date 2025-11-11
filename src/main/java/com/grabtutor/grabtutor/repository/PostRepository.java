package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.Post;
import com.grabtutor.grabtutor.entity.Subject;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findByUserId(String userId);
    Page<Post> findAllByIsDeletedFalse(Pageable pageable);
    @Query("SELECT p FROM Post p WHERE p.isDeleted = false AND p.description LIKE %:keyword%")
    Page<Post> searchDescription(@Param("keyword") String keyword, Pageable pageable);
    Optional<Post> findByChatRoomId(String roomId);
    Page<Post> findByUserId(String userId, Pageable pageable);
    Page<Post> findBySubjectAndIsDeletedFalse(Subject subject, Pageable pageable);
}
