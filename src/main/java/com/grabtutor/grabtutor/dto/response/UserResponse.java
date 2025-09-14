package com.grabtutor.grabtutor.dto.response;

import com.grabtutor.grabtutor.entity.Role;
import com.grabtutor.grabtutor.enums.UserStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    String id;
    String username;
    LocalDate dob;
    String email;
    String phoneNumber;
    boolean isActive;
    UserStatus userStatus;
    LocalDate createdAt;
    LocalDate updatedAt;
    Set<Role> roles;
}
