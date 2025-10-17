package com.grabtutor.grabtutor.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
public enum UserStatus {
    PENDING(0),
    NORMAL(1),
    WARNING(2),
    BANNED(3)
    ;

    private final int level;
    UserStatus(int level) {
        this.level = level;
    }
}
