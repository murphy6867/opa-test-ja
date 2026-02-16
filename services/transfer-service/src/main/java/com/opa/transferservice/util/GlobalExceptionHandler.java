package com.opa.transferservice.util;

import com.opa.transferservice.dto.ApiError;
import com.opa.transferservice.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PolicyDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handlePolicyDenied(PolicyDeniedException ex) {
    int status = (ex.getHttpStatus() > 0) ? ex.getHttpStatus() : 403;
    String traceId = UUID.randomUUID().toString();

    return ResponseEntity.status(status)
        .body(ApiResponse.<Void>builder()
            .success(false)
            .message(ex.getMessage())
            .errors(List.of(ApiError.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build()))
            .data(null)
            .traceId(traceId)
            .timestamp(Instant.now())
            .build());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handle(RuntimeException ex) {
    String traceId = UUID.randomUUID().toString();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.<Void>builder()
            .success(false)
            .message(ex.getMessage())
            .errors(List.of(ApiError.builder()
                .code("INTERNAL_ERROR")
                .message(ex.getMessage())
                .build()))
            .data(null)
            .traceId(traceId)
            .timestamp(Instant.now())
            .build());
    }
}
