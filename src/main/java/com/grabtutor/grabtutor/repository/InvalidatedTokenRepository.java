package com.grabtutor.grabtutor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.grabtutor.grabtutor.entity.InvalidatedToken;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
}
