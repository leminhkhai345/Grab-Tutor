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
public class TutorInfoRequest {

    String userId;
    @NotBlank(message = "national id must not be blank")
    String nationalId;
    @NotBlank(message = "university must not be blank")
    String university;
    @NotBlank(message = "highest academic degree must not be blank")
    String highestAcademicDegree;
    @NotBlank(message = "major must not be blank")
    String major;
}
