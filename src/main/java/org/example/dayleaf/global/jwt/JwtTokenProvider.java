package org.example.dayleaf.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.example.dayleaf.domain.member.entity.MemberRole;
import org.example.dayleaf.global.exception.CustomException;
import org.example.dayleaf.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessExpiry;
    private final long refreshExpiry;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiry}") long accessExpiry,
            @Value("${jwt.refresh-expiry}") long refreshExpiry
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpiry = accessExpiry;
        this.refreshExpiry = refreshExpiry;
    }

    public String createAccessToken(Long memberId, MemberRole role) {
        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .claim("type", "ACCESS")
                .claim("role", role.name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpiry))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long memberId) {
        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .claim("type", "REFRESH")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiry))
                .signWith(key)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        if (!"ACCESS".equals(claims.get("type", String.class))) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        Long memberId = Long.parseLong(claims.getSubject());
        String role = claims.get("role", String.class);
        return new UsernamePasswordAuthenticationToken(
                memberId,
                token,
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }

    public Long getMemberIdFromToken(String token) {
        if (!validateToken(token)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        return Long.parseLong(parseClaims(token).getSubject());
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
