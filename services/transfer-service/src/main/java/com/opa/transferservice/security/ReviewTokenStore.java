package com.opa.transferservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ReviewTokenStore {

    private final StringRedisTemplate redisTemplate;
    private static final String TOKEN_PREFIX = "review:";

    public void save(String jti, Duration ttl) {
        redisTemplate.opsForValue()
                .set(TOKEN_PREFIX + jti, "UNUSED", ttl);
    }

    public boolean consume(String jti) {
        String key = TOKEN_PREFIX + jti;

        Boolean exists = redisTemplate.hasKey(key);
        if (Boolean.FALSE.equals(exists)) {
            return false;
        }

        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key + ":used", "1", Duration.ofMinutes(10));

        if (Boolean.FALSE.equals(success)) {
            return false;
        }

        return true;
    }

}
