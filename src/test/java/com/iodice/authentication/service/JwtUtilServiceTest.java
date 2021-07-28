package com.iodice.authentication.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilServiceTest {
    private JwtUtilService underTest;
    private String usernameExample;
    private String jwtForUsernameExample;
    private String jwtNotForUsernameExample;
    private String jwtExpiredForUsernameExample;
    private User user;

    @BeforeEach
    public void setUp() {
        underTest = new JwtUtilService();
        usernameExample = "Mattia";
        jwtForUsernameExample = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(usernameExample)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact();
        jwtNotForUsernameExample = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject("Other")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact();
        jwtExpiredForUsernameExample = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(usernameExample)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact();
        user = new User(usernameExample, "PasswordExample", Collections.emptyList());
    }

    @Test
    void extractUsernameShouldReturnACorrectUsernameWhenTokenIsValid() {
        final String actualOutput = underTest.extractUsername(jwtForUsernameExample);
        assertNotNull(actualOutput);
        assertEquals(actualOutput, usernameExample);
    }

    @Test
    void extractUsernameShouldThrowAnExpiredJwtExceptionWhenTokenIsExpired() {
        assertThrows(ExpiredJwtException.class, () -> underTest.extractUsername(jwtExpiredForUsernameExample));
    }

    @Test
    void generateTokenShouldReturnANotNullToken() {
        final String actualOutput = underTest.generateToken(usernameExample);
        assertNotNull(actualOutput);
        assertFalse(actualOutput.isEmpty());
    }

    @Test
    void validateTokenShouldReturnTrueWhenUsernameAndTokenUsernameAreEquals() {
        final boolean actualOutput = underTest.validateToken(jwtForUsernameExample, user);
        assertTrue(actualOutput);
    }

    @Test
    void validateTokenShouldReturnFalseWhenUsernameAndTokenUsernameAreDifferent() {
        final boolean actualOutput = underTest.validateToken(jwtNotForUsernameExample, user);
        assertFalse(actualOutput);
    }

    @Test
    void validateTokenShouldThrowAnExpiredJwtExceptionWhenTokenIsCorrectForUsernameButExpired() {
        assertThrows(ExpiredJwtException.class, () -> underTest.validateToken(jwtExpiredForUsernameExample, user));
    }
}