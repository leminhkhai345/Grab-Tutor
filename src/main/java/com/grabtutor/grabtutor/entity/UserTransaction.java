package com.grabtutor.grabtutor.entity;

import com.grabtutor.grabtutor.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "user_transactions")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTransaction extends BaseEntity {
    double amount;

    @Builder.Default
    TransactionStatus status = TransactionStatus.PENDING;

    @OneToOne(mappedBy = "userTransaction")
    Post post;

    @ManyToOne
    @JoinColumn(name = "senderId")
    AccountBalance sender;

    @ManyToOne
    @JoinColumn(name = "receiverId")
    AccountBalance receiver;
}
