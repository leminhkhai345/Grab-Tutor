package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.ChatBox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatBoxRepository extends JpaRepository<ChatBox, String> {
}
