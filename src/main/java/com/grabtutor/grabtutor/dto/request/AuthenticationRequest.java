package com.grabtutor.grabtutor.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AuthenticationRequest {
    @NotBlank(message = "Email must not be blank")
    String email;
    @NotBlank(message = "Password must not be blank")
    String password;

}
