package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.dto.response.MessageResponse;
import com.grabtutor.grabtutor.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findByChatRoomId(String roomId);
}
