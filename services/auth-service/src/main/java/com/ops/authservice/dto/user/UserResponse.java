package com.ops.authservice.dto.user;

import com.ops.authservice.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {

    private UUID id;
    private String username;
    private BigDecimal balance;
    private String accountName;
    private String accountNumber;
    private UserRole role;
    private String bankCode;
    private String bankName;
}
