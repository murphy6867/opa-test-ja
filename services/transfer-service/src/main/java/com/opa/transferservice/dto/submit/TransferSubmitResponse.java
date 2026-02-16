package com.opa.transferservice.dto.submit;

import com.opa.transferservice.enums.TransferEventType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferSubmitResponse {

    @NotNull
    private String transactionId;

    @NotNull
    private TransferEventType status;

    @NotNull
    private Instant submittedAt;
}
