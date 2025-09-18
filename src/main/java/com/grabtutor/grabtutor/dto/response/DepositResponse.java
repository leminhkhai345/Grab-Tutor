package com.grabtutor.grabtutor.dto.response;

import com.grabtutor.grabtutor.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DepositResponse extends BaseEntity {
    String transactionId;
    String transactionNo;
    String transactionInfo;
    String paymentMethod;
    boolean isSuccess;
    String totalAmount;
}
