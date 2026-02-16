package com.ops.authservice.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DebitAccountRequest {

    @NotBlank
    private String accountNumber;

    @NotNull
    private BigDecimal amount;

    private String reference;
}
