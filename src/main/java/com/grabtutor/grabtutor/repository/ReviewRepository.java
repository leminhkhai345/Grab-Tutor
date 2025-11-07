package com.grabtutor.grabtutor.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.grabtutor.grabtutor.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {
    Review findByIdAndIsDeletedFalse(String id);
    Page<Review> findByPostIdAndIsDeletedFalse(String postId, Pageable pageable);
    Page<Review> findBySenderIdAndIsDeletedFalse(String senderId, Pageable pageable);
    Page<Review> findByReceiverIdAndIsDeletedFalse(String receiverId, Pageable pageable);
    boolean existsByPostIdAndSenderIdAndIsDeletedFalse(String postId, String userId);
}
