package com.grabtutor.grabtutor.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatus {

    NORMAL(1),
    WARNING(2),
    BANNED(3)

    ;

    private int level;

}
