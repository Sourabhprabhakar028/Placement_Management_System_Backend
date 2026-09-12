package com.placement.placement.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    // ✅ Generate Access Token (15 mins)
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Generate Refresh Token (random UUID)
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    // ✅ Get Email from Token
    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    // ✅ Get Role from Token
    public String getRoleFromToken(String token) {
        return getClaims(token).get("role", String.class);
    }

    // ✅ Validate Token
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // ✅ Check if Token is Expired
    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // ✅ Get Refresh Token Expiration
    public Long getRefreshExpiration() {
        return refreshExpiration;
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
