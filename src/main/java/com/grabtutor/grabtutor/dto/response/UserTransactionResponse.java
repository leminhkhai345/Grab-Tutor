package com.grabtutor.grabtutor.dto.response;

import com.grabtutor.grabtutor.enums.TransactionStatus;
import com.grabtutor.grabtutor.enums.UserTransactionType;
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
    double amount;
    String courseId;
    String postId;
    UserTransactionType type;
    TransactionStatus status;
    String senderId;
    String receiverId;
    LocalDateTime createdAt;

}
