package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.AccountBalance;
import com.grabtutor.grabtutor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance,String> {
    Optional<AccountBalance> findByUserId(String userId);

}
