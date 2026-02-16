package com.opa.transferservice.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSnapshot {

    private String fromAccount;
    private String toAccount;
    private String bankCode;
    private BigDecimal amount;
    private String summary;
    private BigDecimal fee;
    private BigDecimal totalAmount;
    private String role;
    private Boolean approvalRequired;

//    public ReviewSnapshot(String fromAccount, String toAccount, String bankCode, BigDecimal amount, String summary) {
//        this.fromAccount = fromAccount;
//        this.toAccount = toAccount;
//        this.bankCode = bankCode;
//        this.amount = amount;
//        this.summary = summary;
//    }
}
