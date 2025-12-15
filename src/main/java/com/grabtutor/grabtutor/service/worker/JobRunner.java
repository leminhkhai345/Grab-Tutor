package com.grabtutor.grabtutor.service.worker;

import com.grabtutor.grabtutor.enums.JobType;
import com.grabtutor.grabtutor.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobRunner {

    private final JobRepository jobRepository;
    private final JobHandler handler;

    @Transactional
    public int runOnce(JobType type) {
        var jobs = jobRepository
                .findTop50ByIsDeletedFalseAndJobTypeAndRunAtLessThanEqualOrderByRunAtAsc(
                        type, LocalDateTime.now()
                );

        for (var job : jobs) {
            try {
                handler.handle(job);
                job.setDeleted(true); // DONE -> soft delete
            } catch (Exception e) {
                log.error("Job failed: type={}, refId={}, id={}", job.getJobType(), job.getRefId(), job.getId(), e);
                // retry sau 30s
                job.setRunAt(LocalDateTime.now().plusSeconds(30));
            }
        }

        return jobs.size();
    }
}
