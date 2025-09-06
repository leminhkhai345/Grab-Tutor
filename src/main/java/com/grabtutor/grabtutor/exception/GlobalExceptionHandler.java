package com.grabtutor.grabtutor.exception;

import com.grabtutor.grabtutor.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException ex) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(false);
        response.setMessage(ex.getErrorCode().getMessage());
        return ResponseEntity.status(ex.getErrorCode().getHttpStatusCode()).body(response);
    }

}
