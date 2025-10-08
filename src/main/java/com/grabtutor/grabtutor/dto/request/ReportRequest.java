package com.grabtutor.grabtutor.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
public class ReportRequest {
    @NotBlank(message = "Detail is required")
    String detail;
}
