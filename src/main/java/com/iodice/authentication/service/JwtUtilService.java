package com.iodice.authentication.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

/**
 * JWT utility service.
 * @author Mattia Iodice
 * @version 1.0.0
 */
@Service
public class JwtUtilService {
    private final String SECRET_KEY = "secret";

    /**
     * Extract username by JWT.
     * @param token JWT
     * @return Username
     */
    public String extractUsername(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Generate token by username.
     * @param username - Username
     * @return JWT
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * Valid JWT username and username.
     * @param token JWT
     * @param userDetails Username
     * @return If usernames are equals true, otherwise false
     */
    public Boolean validateToken(String token, UserDetails userDetails) throws ExpiredJwtException {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername());
    }
}