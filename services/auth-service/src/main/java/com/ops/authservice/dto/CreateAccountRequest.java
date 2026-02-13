package com.ops.authservice.dto;

import com.ops.authservice.enums.BankCode;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAccountRequest {

    @NonNull
    private String username;

    @NonNull
    private String accountName;

    @NonNull
    private BankCode bankCode;

    @NonNull
    @PositiveOrZero
    private BigDecimal initialBalance;
}
