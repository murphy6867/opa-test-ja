package com.opa.transferservice.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitAccountResponse {
    private String accountNumber;
    private BigDecimal debitedAmount;
    private BigDecimal newBalance;
}
