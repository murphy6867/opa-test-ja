package com.ops.authservice.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class AccountResponse {
    private UUID id;
    private UUID userId;
    private String username;
    private String accountName;
    private String accountNumber;
    private String bankCode;
    private String bankName;
    private BigDecimal balance;
}
