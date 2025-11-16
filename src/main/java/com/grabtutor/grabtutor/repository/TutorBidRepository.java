package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.Post;
import com.grabtutor.grabtutor.entity.TutorBid;
import com.grabtutor.grabtutor.enums.BiddingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TutorBidRepository extends JpaRepository<TutorBid, String> {
    List<TutorBid> findByPostIdOrderByCreatedAtDesc(String postId);
    Page<TutorBid> findAllByIsDeletedFalseAndUserId(String userId,Pageable pageable);
}
