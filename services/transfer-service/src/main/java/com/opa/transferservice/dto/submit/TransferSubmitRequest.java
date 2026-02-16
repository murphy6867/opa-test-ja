package com.opa.transferservice.dto.submit;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferSubmitRequest {

    @NotBlank
    private String reviewToken;

    @NotBlank
    private String otpCode;
}
