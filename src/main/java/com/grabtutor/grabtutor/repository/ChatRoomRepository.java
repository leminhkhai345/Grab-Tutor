package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
}
