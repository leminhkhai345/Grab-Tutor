package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.Notification;
import com.grabtutor.grabtutor.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {
    Page<Notification> findAllByIsDeletedFalseAndUserId(String userId ,Pageable pageable);
    List<Notification> findAllByIsDeletedFalseAndUserId(String userId);
}
