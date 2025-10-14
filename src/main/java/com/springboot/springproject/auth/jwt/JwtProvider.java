package com.springboot.springproject.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private static final String SECRET = "";
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final long EXPIRATION = 1000 * 60 * 60; // 1시간

    public String generateToken(String username, String role, String provider) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role) // 권한
                .claim("provider", provider)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public String getEmail(String token) {
        return parseToken(token).getBody().getSubject();
    }

    public String getProvider(String token) {
        return (String) parseToken(token).getBody().get("provider");
    }

    public String getRole(String token) {
        return (String) parseToken(token).getBody().get("role");
    }
}
