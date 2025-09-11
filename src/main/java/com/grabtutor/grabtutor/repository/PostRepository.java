package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findByUserId(String userId);
}
