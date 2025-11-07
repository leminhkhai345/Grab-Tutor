package com.grabtutor.grabtutor.dto.response;

import com.grabtutor.grabtutor.enums.TransactionStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTransactionResponse {
    String id;
    String postId;
    double amount;
    TransactionStatus status;
    String senderId;
    String receiverId;
    LocalDateTime creationAt;
    LocalDateTime updatedAt;
}
