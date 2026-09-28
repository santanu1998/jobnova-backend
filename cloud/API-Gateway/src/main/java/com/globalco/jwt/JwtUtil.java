package com.globalco.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtUtil {
    @Value("${JWT_SECRET}")
    private String jwtSecret;

    private SecretKey secretKey;

    public SecretKey getSecretKey() {
        if (secretKey == null && jwtSecret != null) {
            secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        }
        return secretKey;
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    public String extractAuthorities(String token) {
        return extractAllClaims(token).get("authorities", String.class);
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}