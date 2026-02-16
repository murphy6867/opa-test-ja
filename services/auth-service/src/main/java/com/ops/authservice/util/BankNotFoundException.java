package com.ops.authservice.util;

public class BankNotFoundException extends RuntimeException {
    public BankNotFoundException(String bankCode) {
        super("Bank not found: " + bankCode);
    }
}
