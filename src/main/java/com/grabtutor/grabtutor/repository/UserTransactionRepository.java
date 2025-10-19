package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.UserTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTransactionRepository extends JpaRepository<UserTransaction, String> {
}
