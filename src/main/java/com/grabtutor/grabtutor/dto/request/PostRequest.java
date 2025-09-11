package com.grabtutor.grabtutor.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
public class PostRequest {
    @NotBlank(message = "Image URL must be not blank")
    String imageUrl;
    @NotBlank(message = "Description must be not blank")
    String description;
}
