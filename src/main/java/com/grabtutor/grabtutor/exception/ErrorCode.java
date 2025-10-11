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
    ACCOUNT_ALREADY_VERIFIED(1006, "Account already verified", HttpStatus.CONFLICT),
    POST_NOT_EXIST(2001, "Post does not exist", HttpStatus.NOT_FOUND),

    SUBJECT_ALREADY_EXISTS(3001, "Subject already exists", HttpStatus.CONFLICT),
    SUBJECT_NOT_FOUND(3002, "Subject not found", HttpStatus.NOT_FOUND),

    INVALID_KEY(4001, "Invalid key", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1007, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_CURRENT_PASSWORD(1009, "Current password is incorrect", HttpStatus.BAD_REQUEST),
    TOKEN_INVALID(4002, "Token is invalid", HttpStatus.UNAUTHORIZED),

    OTP_INVALID(5001, "OTP invalid", HttpStatus.BAD_REQUEST),
    OTP_EXPIRED(5002, "OTP expired", HttpStatus.BAD_REQUEST),
    OTP_USED(5003, "OTP used", HttpStatus.BAD_REQUEST),
    SESSION_END(5004, "Session ended", HttpStatus.BAD_REQUEST),


    REPORT_NOT_EXIST(6001, "Report does not exist", HttpStatus.NOT_FOUND),

    REVIEW_NOT_EXIST(7001, "Review does not exist", HttpStatus.NOT_FOUND),

    CHAT_ROOM_NOT_FOUND(8001, "Chat room not found", HttpStatus.NOT_FOUND),

    FORBIDDEN(88888, "You do not have permission to access this resource", HttpStatus.FORBIDDEN),
    UNCATEGORIZED(99999, "Uncategorized", HttpStatus.BAD_REQUEST),
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
