package com.grabtutor.grabtutor.exception;


import lombok.Getter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;



@RequiredArgsConstructor
@Getter
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
}

