package com.grabtutor.grabtutor.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
public class ReviewRequest {
    @Size(min = 1, max = 5, message = "Rating must be between 1 and 5")
    @NotBlank(message = "Rating must be not blank")
    int stars;
    @NotBlank(message = "Description must be not blank")
    String description;
}
