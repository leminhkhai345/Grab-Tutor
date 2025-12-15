package com.grabtutor.grabtutor.entity;

import com.grabtutor.grabtutor.enums.JobType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "jobs", indexes = {
        @Index(name = "idx_jobs_due", columnList = "isDeleted, jobType, runAt")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Job extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private JobType jobType;

    @Column(nullable = false, length = 100)
    private String refId;

    @Column(nullable = false)
    private LocalDateTime runAt;
}
