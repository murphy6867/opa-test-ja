package com.ops.authservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BankCode {
    BBL("002"),
    KBANK("004"),
    KTB("006"),
    SCB("014"),
    BAY("025");

    private final String code;

    BankCode(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static BankCode fromCode(String input) {
        if (input == null) {
            return null;
        }

        String normalized = input.trim();
        for (BankCode bank : values()) {
            if (bank.code.equals(normalized) || bank.name().equalsIgnoreCase(normalized)) {
                return bank;
            }
        }
        throw new IllegalArgumentException("Invalid bank code: " + input);
    }
}
