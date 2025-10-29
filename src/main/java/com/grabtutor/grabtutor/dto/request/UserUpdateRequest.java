package com.grabtutor.grabtutor.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
public class UserUpdateRequest {
    @NotBlank(message = "full name must be not blank")
    String fullName;
    LocalDate dob;
    @Size(min = 10, max = 10, message = "phone number must be 10 characters")
    String phoneNumber;
}
