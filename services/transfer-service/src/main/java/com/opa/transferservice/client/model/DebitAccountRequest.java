package com.opa.transferservice.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitAccountRequest {
    private String accountNumber;
    private BigDecimal amount;
    private String reference;
}
