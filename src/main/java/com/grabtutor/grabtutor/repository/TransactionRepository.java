package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,String> {
}
