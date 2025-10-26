package com.grabtutor.grabtutor.dto.response;

import com.grabtutor.grabtutor.enums.Role;
import com.grabtutor.grabtutor.enums.UserStatus;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TutorResponse {
    String id;
    LocalDate dob;
    String fullName;
    String email;
    String phoneNumber;
    boolean isActive;
    String userStatus;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Role role;
    String nationalId;
    String university;
    String highestAcademicDegree;
    String major;
}
