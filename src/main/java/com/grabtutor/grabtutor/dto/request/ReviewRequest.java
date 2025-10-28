package com.grabtutor.grabtutor.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
public class ReviewRequest {
    @Min(1)
    @Max(5)
    @NotNull(message = "Stars must be not null")
    int stars;
    @NotBlank(message = "Description must be not blank")
    String description;
}
