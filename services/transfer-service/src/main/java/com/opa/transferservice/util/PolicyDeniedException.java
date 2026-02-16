package com.opa.transferservice.util;

public class PolicyDeniedException extends RuntimeException {

    private final String code;
    private final int httpStatus;

    public PolicyDeniedException(String code, String message, int httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
