package com.grabtutor.grabtutor.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {


    ;
    private final long code;
    private final String message;

    ErrorCode(long code, String message) {
        this.code = code;
        this.message = message;
    }
}
