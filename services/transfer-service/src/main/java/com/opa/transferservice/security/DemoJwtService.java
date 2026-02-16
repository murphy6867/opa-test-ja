package com.opa.transferservice.security;

import com.opa.transferservice.security.model.ReviewSnapshot;
import com.opa.transferservice.security.model.TokenPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class DemoJwtService implements JwtService {

    private final String SECRET = "demo-payment-review-secret-key-which-should-be-long-enough";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    @Override
    public String generate(String jti, String userId, ReviewSnapshot snapshot) {

        return Jwts.builder()
                .id(jti)
                .subject(userId)
                .claim("snapshot", snapshot)
                .issuedAt(new Date())
                .expiration(Date.from(
                        Instant.now().plus(5, ChronoUnit.MINUTES)
                ))
                .signWith(key)
                .compact();
    }

    @Override
    public TokenPayload verify(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String jti = claims.getId();
        String userId = claims.getSubject();

        ReviewSnapshot snapshot =
                new ObjectMapper().convertValue(
                        claims.get("snapshot"),
                        ReviewSnapshot.class
                );

        return new TokenPayload(jti, userId, snapshot);
    }
}
