package com.ops.authservice.dto.account;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DebitAccountResponse {
    private String accountNumber;
    private BigDecimal debitedAmount;
    private BigDecimal newBalance;
}
