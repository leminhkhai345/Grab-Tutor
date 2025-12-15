package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.Job;
import com.grabtutor.grabtutor.enums.JobType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, String> {

    Optional<Job> findByIsDeletedFalseAndJobTypeAndRefId(JobType jobType, String refId);

    List<Job> findTop50ByIsDeletedFalseAndJobTypeAndRunAtLessThanEqualOrderByRunAtAsc(
            JobType jobType, LocalDateTime now
    );
}
