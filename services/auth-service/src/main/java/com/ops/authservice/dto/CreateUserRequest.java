package com.ops.authservice.dto;

import com.ops.authservice.enums.BankCode;
import com.ops.authservice.enums.UserRole;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {


    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private String accountName;

    @NonNull
    private UserRole role;

    @NonNull
    private BankCode bankCode;

    @NonNull
    @PositiveOrZero
    private BigDecimal balance;
}
