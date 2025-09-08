package com.grabtutor.grabtutor.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
public class UserRequest {
    @Size(min = 5, max = 20, message = "username must be between 5 and 20 characters")
    @NotBlank(message = "username must be not blank")
    String username;
    @NotBlank(message = "password must be not blank")
    @Size(min = 5, message = "password must be at least 8 characters")
    String password;
    LocalDate dob;
    @NotBlank(message = "email must be not blank")
    @Email(message = "email not valid")
    String email;
    @Size(min = 10, max = 10, message = "phone number must be 10 characters")
    String phoneNumber;
}
