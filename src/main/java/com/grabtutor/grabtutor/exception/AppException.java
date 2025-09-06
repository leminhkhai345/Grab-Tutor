package com.grabtutor.grabtutor.exception;


import lombok.Getter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;



@Getter
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}

