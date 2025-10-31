package com.grabtutor.grabtutor.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grabtutor.grabtutor.enums.TransactionStatus;
import com.grabtutor.grabtutor.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


import java.time.LocalDateTime;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VirtualTransactionResponse {
    String userId;
    TransactionStatus status;
    TransactionType type;
    LocalDateTime transactionDate;
    LocalDateTime completedAt;
    Double amount;
}
