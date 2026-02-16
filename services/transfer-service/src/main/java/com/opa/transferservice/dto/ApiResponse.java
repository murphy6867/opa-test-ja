package com.opa.transferservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private List<ApiError> errors;
    private T data;
    private String traceId;
    private Instant timestamp;
}
