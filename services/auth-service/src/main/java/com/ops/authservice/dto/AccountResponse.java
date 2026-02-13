package com.ops.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class AccountResponse {
    private Long id;
    private Long userId;
    private String username;
    private String accountName;
    private String accountNumber;
    private String bankCode;
    private BigDecimal balance;
}
