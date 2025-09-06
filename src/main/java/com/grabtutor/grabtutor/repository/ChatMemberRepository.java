package com.grabtutor.grabtutor.repository;
import com.grabtutor.grabtutor.entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMemberRepository extends JpaRepository<ChatMember, String> {
}
