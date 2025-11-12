package com.grabtutor.grabtutor.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusStatistic {
    int total;
    int PENDING, NORMAL, WARNING, BANNED;
}
