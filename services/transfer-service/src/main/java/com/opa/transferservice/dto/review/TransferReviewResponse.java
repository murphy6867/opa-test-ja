package com.opa.transferservice.dto.review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferReviewResponse {

    @NotBlank
    private String fromAccountName;

    @NotBlank
    private String toAccountName;

    @NotBlank
    private String toBankCode;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private BigDecimal fee;

    @NotNull
    private BigDecimal totalAmount;

    private BigDecimal exchangeRate;

    private boolean approvalRequired;

    @NotBlank
    private String reviewToken;
}