package com.grabtutor.grabtutor.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.grabtutor.grabtutor.entity.Report;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, String> {
    Page<Report> findByUserIdAndIsDeletedFalse(String userId,Pageable pageable);
}
