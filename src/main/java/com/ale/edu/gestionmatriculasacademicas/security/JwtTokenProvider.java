package com.ale.edu.gestionmatriculasacademicas.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @PostConstruct
    public void init() {
        
        log.info("JWT_SECRET length: {} ::: {}", (jwtSecret != null ? jwtSecret.length() : "null"), jwtSecret);
        log.error("JWT_SECRET length: {}", jwtSecret != null ? jwtSecret.length() : "null");
        log.error("JWT_SECRET::: {}", jwtSecret);
    }

    public String generateToken(Authentication authentication) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        JwtBuilder builder = Jwts.builder()
            .setSubject(authentication.getName())
            .claim("auth", authorities)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration));

        if (authentication.getPrincipal() instanceof com.ale.edu.gestionmatriculasacademicas.domain.User) {
            com.ale.edu.gestionmatriculasacademicas.domain.User user = (com.ale.edu.gestionmatriculasacademicas.domain.User) authentication.getPrincipal();
            builder.claim("id", user.getId());
        }

        return builder.signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

    public String getUsernameFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
