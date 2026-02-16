package com.ops.authservice.dto.account;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String bankCode;

    @NonNull
    @PositiveOrZero
    private BigDecimal initialBalance;
}
