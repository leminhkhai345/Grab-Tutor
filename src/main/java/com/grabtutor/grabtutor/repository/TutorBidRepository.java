package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.TutorBid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TutorBidRepository extends JpaRepository<TutorBid, String> {
    List<TutorBid> findByPostId(String postId);
}
