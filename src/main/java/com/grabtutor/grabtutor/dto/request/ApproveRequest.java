package com.grabtutor.grabtutor.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApproveRequest {
    String requestId;
}
