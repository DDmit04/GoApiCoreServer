package com.goapi.goapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${authorization.jwt.secret}")
    private String jwtSecret;
    @Value("${tokens.life.access}")
    private int commonJwtLifetime;
    @Value("${tokens.life.refresh}")
    private int refreshJwtLifetime;

    private String generateToken(String username, int seconds) {
        Date date = Timestamp.valueOf(LocalDateTime.now().plusSeconds(seconds));
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateAccessToken(String username) {
        return generateToken(username, commonJwtLifetime);
    }

    public String generateRefreshToken(String str) {
        return generateToken(str, refreshJwtLifetime);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getTokenSubject(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }
}