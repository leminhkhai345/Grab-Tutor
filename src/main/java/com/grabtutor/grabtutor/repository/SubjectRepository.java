package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
}
