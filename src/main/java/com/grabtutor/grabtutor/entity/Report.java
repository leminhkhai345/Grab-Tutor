package com.grabtutor.grabtutor.entity;

import com.grabtutor.grabtutor.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "reports")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Report extends BaseEntity {

    String detail;
    ReportStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderId")
    User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverId")
    User receiver;
    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    Post post;

}
