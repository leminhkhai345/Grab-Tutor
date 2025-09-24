package com.grabtutor.grabtutor.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ChangeForgotPasswordRequest {

    @NotBlank
    String email;

    @NotBlank(message = "please enter new password")
    @Size(min = 5, message = "new password must be at least 5 characters")
    String newPassword;

    @NotBlank(message = "please confirm new password")
    String confirmPassword;
}
