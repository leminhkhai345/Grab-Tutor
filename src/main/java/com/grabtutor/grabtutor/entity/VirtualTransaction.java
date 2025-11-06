package com.grabtutor.grabtutor.entity;

import com.grabtutor.grabtutor.enums.TransactionStatus;
import com.grabtutor.grabtutor.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class VirtualTransaction extends BaseEntity {


    @Column(nullable = false)
    Double amount; // Số tiền đã thanh toán

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TransactionStatus status; // PENDING, SUCCESS, FAILED

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TransactionType type;

    @CreationTimestamp
    @Column(nullable = false)
    LocalDateTime transactionDate;

    LocalDateTime completedAt;

    @ManyToOne
    @JoinColumn(name = "course_id")
    Course course;

    @ManyToOne
    @JoinColumn(name = "post_id")
    Post post;

    @ManyToOne
    @JoinColumn(name = "account_balance_id")
    AccountBalance accountBalance;

}
