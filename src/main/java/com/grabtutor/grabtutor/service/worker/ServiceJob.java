package com.grabtutor.grabtutor.service.worker;

import com.grabtutor.grabtutor.entity.Job;
import com.grabtutor.grabtutor.enums.JobType;
import com.grabtutor.grabtutor.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ServiceJob {

    private final JobRepository jobRepository;

    @Transactional
    public void addCheckPost(String postId, LocalDateTime createdTime) {
        upsert(JobType.POST_EXPIRE, postId, createdTime.plusMinutes(30));
    }

    @Transactional
    public void addCheckRoomTimeout(String roomId, LocalDateTime createdTime) {
        upsert(JobType.CHATROOM_TIMEOUT, roomId, createdTime.plusMinutes(15));
    }

    @Transactional
    public void addCheckRoomConfirmed(String roomId, LocalDateTime createdTime) {
        upsert(JobType.CHATROOM_CONFIRMED, roomId, createdTime.plusMinutes(15));
    }

    private void upsert(JobType type, String refId, LocalDateTime runAt) {
        Job job = jobRepository.findByIsDeletedFalseAndJobTypeAndRefId(type, refId)
                .orElseGet(() -> Job.builder()
                        .jobType(type)
                        .refId(refId)
                        .build());

        job.setRunAt(runAt);
        job.setDeleted(false);
        jobRepository.save(job);
    }
}
