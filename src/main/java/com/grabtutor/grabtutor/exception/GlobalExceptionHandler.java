package com.grabtutor.grabtutor.exception;

import com.grabtutor.grabtutor.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setCode(ErrorCode.UNCATEGORIZED.getCode());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException ex) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(false);
        response.setMessage(ex.getErrorCode().getMessage());
        response.setCode(ex.getErrorCode().getCode());
        return ResponseEntity.status(ex.getErrorCode().getHttpStatusCode()).body(response);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;
        try {
            errorCode = ErrorCode.valueOf(enumKey);

            var constraintViolation =
                    exception.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

            attributes = constraintViolation.getConstraintDescriptor().getAttributes();

            log.info(attributes.toString());

        } catch (Exception e) {}

        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(
                Objects.nonNull(attributes)
                        ? mapAttribute(errorCode.getMessage(), attributes)
                        : exception.getBindingResult().getFieldErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .findFirst()
                        .orElse(exception.getMessage()));
        apiResponse.setSuccess(false);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    ResponseEntity<ApiResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setCode(ErrorCode.MISSING_PARAMETER.getCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(value = IllegalStateException.class)
    ResponseEntity<ApiResponse> handleIllegalStateException(IllegalStateException ex) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setCode(ErrorCode.UNCATEGORIZED.getCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
