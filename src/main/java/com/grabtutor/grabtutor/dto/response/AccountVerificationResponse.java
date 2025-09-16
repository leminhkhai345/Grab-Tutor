package com.grabtutor.grabtutor.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
public class AccountVerificationResponse {
    String userId;
    String requestId;
}
