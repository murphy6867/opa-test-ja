package com.opa.transferservice.enums;

public enum TransferEventType {
    SUBMITTED,
    OPA_APPROVED,
    OPA_DENIED,

    WAITING_APPROVAL,
    APPROVED,
    REJECTED,

    PROCESSING,
    DEBIT_SUCCESS,
    CREDIT_SUCCESS,

    COMPLETED,
    FAILED
}
