package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.TutorInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TutorInfoRepository extends JpaRepository<TutorInfo, String> {
    boolean existsByNationalId(String nationalId);
    Optional<TutorInfo> findByNationalId(String nationalId);
}
