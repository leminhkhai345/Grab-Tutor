package com.grabtutor.grabtutor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.grabtutor.grabtutor.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {
    Review findByIdAndIsDeletedFalse(String id);
    List<Review> findByPostIdAndIsDeletedFalse(String postId);
    List<Review> findBySenderIdAndIsDeletedFalse(String senderId);
}
