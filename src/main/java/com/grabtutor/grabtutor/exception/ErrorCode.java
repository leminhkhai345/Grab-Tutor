package com.grabtutor.grabtutor.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(1001, "User not found", HttpStatus.NOT_FOUND),
    USERNAME_ALREADY_EXISTS(1002, "Username already exists", HttpStatus.CONFLICT),
    EMAIL_ALREADY_EXISTS(1003, "Email already exists", HttpStatus.CONFLICT),
    UNAUTHENTICATED(1004, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    NATIONAL_ID_ALREADY_EXISTS(1005, "National id already exists", HttpStatus.CONFLICT),
    POST_NOT_EXIST(2001, "Post does not exist", HttpStatus.NOT_FOUND),



    SUBJECT_ALREADY_EXISTS(3001, "Subject already exists", HttpStatus.CONFLICT),
    SUBJECT_NOT_FOUND(3002, "Subject not found", HttpStatus.NOT_FOUND),

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
