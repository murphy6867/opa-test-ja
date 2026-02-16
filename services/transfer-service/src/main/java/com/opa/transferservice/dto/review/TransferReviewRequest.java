package com.opa.transferservice.dto.review;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferReviewRequest {

    @NotBlank
    private UUID userId;

    @NotBlank
    private String fromAccount;

    @NotBlank
    private String toAccount;

    @NotNull
    private String bankCode;

    @NotNull
    @Positive
    @Digits(integer = 15, fraction = 2)
    private BigDecimal amount;

    @Size(max = 255)
    private String note;
}
