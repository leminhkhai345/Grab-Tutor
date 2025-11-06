package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.VirtualTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VirtualTransactionRepository extends JpaRepository<VirtualTransaction, String> {
    Page<VirtualTransaction> findAllByAccountBalanceId(String userId, Pageable pageable);
}
