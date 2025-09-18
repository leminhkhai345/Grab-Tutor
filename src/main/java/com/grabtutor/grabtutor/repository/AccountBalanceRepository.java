package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance,String> {
}
