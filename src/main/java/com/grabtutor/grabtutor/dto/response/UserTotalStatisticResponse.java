package com.grabtutor.grabtutor.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTotalStatisticResponse {
    int totalUsers;
    int tutors, students, admins;
}
