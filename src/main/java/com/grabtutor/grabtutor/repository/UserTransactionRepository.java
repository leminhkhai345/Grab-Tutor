package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.UserTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTransactionRepository extends JpaRepository<UserTransaction, String> {
    Page<UserTransaction> findAllBySenderIdOrReceiverId(String senderId, String receiverId, Pageable pageable);
}
