package com.grabtutor.grabtutor.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
public class UserRequest {
    @NotBlank(message = "email must be not blank")
    @Email(message = "email not valid")
    String email;
    @NotBlank(message = "password must be not blank")
    @NotNull
    @Size(min = 5, message = "INVALID_PASSWORD")
    String password;
    LocalDate dob;

    @Size(min = 10, max = 10, message = "phone number must be 10 characters")
    String phoneNumber;
}
