package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.VerificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRequestRepository extends JpaRepository<VerificationRequest, String> {
}
