package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, String> {
}
