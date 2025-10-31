package com.grabtutor.grabtutor.dto.response;

import com.grabtutor.grabtutor.entity.BaseEntity;
import com.grabtutor.grabtutor.enums.TransactionStatus;
import com.grabtutor.grabtutor.enums.TransactionType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TransactionResponse extends BaseEntity {
    String id;
    String userId;
    TransactionStatus status;
    TransactionType type;
    LocalDateTime transactionDate;
    LocalDateTime completedAt;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    double amount;
}
