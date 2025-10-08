package com.grabtutor.grabtutor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.grabtutor.grabtutor.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, String> {
    Review findByIdAndIsDeletedFalse(String id);
}
