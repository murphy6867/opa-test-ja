package com.opa.transferservice.security;

import com.opa.transferservice.security.model.ReviewSnapshot;
import com.opa.transferservice.security.model.TokenPayload;

public interface JwtService {
    String generate(String jti, String userId, ReviewSnapshot snapshot);
    TokenPayload verify(String token);
}
