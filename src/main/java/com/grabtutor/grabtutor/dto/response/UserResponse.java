package com.grabtutor.grabtutor.dto.response;

import com.grabtutor.grabtutor.enums.UserStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    String id;
    LocalDate dob;
    String fullName;
    String email;
    String phoneNumber;
    boolean isActive;
    UserStatus userStatus;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String role;
}
