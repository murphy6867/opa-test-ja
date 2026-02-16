package com.opa.transferservice.security;

import com.opa.transferservice.security.model.ReviewSnapshot;
import com.opa.transferservice.security.model.TokenPayload;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReviewTokenService {

    private final ReviewTokenStore reviewTokenStore;
    private final JwtService jwtService;

    public String generateToken(UUID userId, ReviewSnapshot snapshot) {

        String jti = UUID.randomUUID().toString();

        reviewTokenStore.save(jti, Duration.ofMinutes(5));

        return jwtService.generate(jti, String.valueOf(userId), snapshot);
    }

    public TokenPayload verifyAndConsume(String token) {

        TokenPayload payload = jwtService.verify(token);

        boolean ok = reviewTokenStore.consume(payload.getJti());

        if (!ok) {
            throw new IllegalStateException("Review token expired, invalid, or already used");
        }

        return payload;
    }
}
