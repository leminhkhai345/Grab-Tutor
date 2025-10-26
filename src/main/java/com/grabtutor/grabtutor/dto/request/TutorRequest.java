package com.grabtutor.grabtutor.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class TutorRequest {
    @NotBlank(message = "email must be not blank")
    @Email(message = "email not valid")
    String email;
    @NotBlank(message = "password must be not blank")
    @NotNull
    @Size(min = 5, message = "INVALID_PASSWORD")
    String password;
    @NotBlank(message = "full name must be not blank")
    String fullName;

    LocalDate dob;
    @NotBlank(message = "phone number must be not blank")
    @Size(min = 10, max = 10, message = "phone number must be 10 characters")
    String phoneNumber;

    @NotBlank(message = "national id must not be blank")
    @Size(min = 12, max = 12, message = "need exactly 12 digits")
    String nationalId;

    @NotBlank(message = "university must not be blank")
    String university;
    @NotBlank(message = "highest academic degree must not be blank")
    String highestAcademicDegree;
    @NotBlank(message = "major must not be blank")
    String major;
}
