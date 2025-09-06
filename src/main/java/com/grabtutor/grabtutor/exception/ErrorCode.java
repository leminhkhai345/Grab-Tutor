package com.grabtutor.grabtutor.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {


    ;
    private final long code;
    private final String message;
    private final HttpStatusCode httpStatusCode;

    ErrorCode(long code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
