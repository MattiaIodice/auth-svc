package com.iodice.authentication.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilServiceTest {
    private JwtUtilService underTest;
    private String usernameExample;
    private String jwtForUsernameExample;

    @BeforeEach
    public void setUp() {
        underTest = new JwtUtilService();
        usernameExample = "Mattia";
        jwtForUsernameExample = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNYXR0aWEiLCJleHAiOjE2MjU4ODE1NDcsImlhdCI6MTYyNTg0NTU0N30.qOk7NFqDNJuK4F7f5xTJA-1IKIbV2FHk4akXeGsAyXg";
    }

    @Test
    void extractUsernameShouldReturnACorrectUsernameWhenTokenIsValid() {
        final String actualOutput = underTest.extractUsername(jwtForUsernameExample);
        assertNotNull(actualOutput);
        assertEquals(actualOutput, usernameExample);
    }

    @Test
    void generateTokenShouldReturnANotNullToken() {
        final String actualOutput = underTest.generateToken(usernameExample);
        assertNotNull(actualOutput);
        assertFalse(actualOutput.isEmpty());
    }

    @Test
    void validateTokenShouldReturnTrueWhenUsernameAndTokenUsernameAreEquals() {
        final User user = new User(usernameExample, "PasswordExample", Collections.emptyList());
        final boolean actualOutput = underTest.validateToken(jwtForUsernameExample, user);
        assertTrue(actualOutput);
    }

    @Test
    void validateTokenShouldReturnFalseWhenUsernameAndTokenUsernameAreDifferent() {
        final User user = new User(usernameExample, "PasswordExample", Collections.emptyList());
        final String jwtNotForUsernameExample = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVc2VyIiwiZXhwIjoxNjI1ODgxNzQ1LCJpYXQiOjE2MjU4NDU3NDV9.xDgUhruKL9UQ8XrX4FV7Ep8hnapEQ1D6WmrGBVCLxf4";
        final boolean actualOutput = underTest.validateToken(jwtNotForUsernameExample, user);
        assertFalse(actualOutput);
    }
}