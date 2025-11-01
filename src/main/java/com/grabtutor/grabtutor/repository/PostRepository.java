package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findByUserId(String userId);
    Page<Post> findAllByIsDeletedFalse(Pageable pageable);
    Optional<Post> findByChatRoomId(String roomId);
}
