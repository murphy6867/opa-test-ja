package com.opa.transferservice.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenPayload {
    private String jti;
    private String userId;
    private ReviewSnapshot snapshot;
}
