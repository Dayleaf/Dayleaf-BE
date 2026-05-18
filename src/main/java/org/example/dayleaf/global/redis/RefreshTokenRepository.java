package org.example.dayleaf.global.redis;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private static final String KEY_PREFIX = "refresh:";

    private final StringRedisTemplate redisTemplate;

    @Value("${jwt.refresh-expiry}")
    private long refreshExpiry;

    public void save(Long memberId, String refreshToken) {
        redisTemplate.opsForValue().set(
                KEY_PREFIX + memberId,
                refreshToken,
                refreshExpiry,
                TimeUnit.MILLISECONDS
        );
    }

    public Optional<String> find(Long memberId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(KEY_PREFIX + memberId));
    }

    public void delete(Long memberId) {
        redisTemplate.delete(KEY_PREFIX + memberId);
    }
}
