package com.grabtutor.grabtutor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.grabtutor.grabtutor.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {
}
