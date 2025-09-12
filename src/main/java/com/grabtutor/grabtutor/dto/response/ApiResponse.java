package com.grabtutor.grabtutor.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T>{
    @Builder.Default
    boolean success = true;
    String message;
    T data;
    @Builder.Default
    private long code = 1000;
    @Builder.Default
    LocalDateTime timestamp = LocalDateTime.now();
}
