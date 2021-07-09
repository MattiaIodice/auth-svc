package com.iodice.authentication.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class CommonExceptionHandlerTest {
    private CommonExceptionHandler underTest;

    @BeforeEach
    public void setUp() {
        underTest = new CommonExceptionHandler();
    }

    @Test
    void handleShouldReturn409WhenARegisterExceptionOccurs() {
        final RegisterException registerException = new RegisterException("RegisterException sample message");
        final ResponseEntity<?> actualOutput = underTest.handleRegisterException(registerException);
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.getStatusCode());
        assertEquals(actualOutput.getStatusCode(), HttpStatus.CONFLICT);
    }

    @Test
    void handleShouldReturn404WhenALoginExceptionOccurs() {
        final LoginException ex = new LoginException("LoginException sample message");
        final ResponseEntity<?> actualOutput = underTest.handleLoginException(ex);
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.getStatusCode());
        assertEquals(actualOutput.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void handleShouldReturn400WhenAConfigurationExceptionOccurs() {
        final ConfigurationException configurationException = new ConfigurationException("ConfigurationException sample message");
        final ResponseEntity<?> actualOutput = underTest.handleConfigurationException(configurationException);
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.getStatusCode());
        assertEquals(actualOutput.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void handleShouldReturn400WhenAnIllegalArgumentExceptionOccurs() {
        final IllegalArgumentException illegalArgumentException = new IllegalArgumentException("IllegalArgumentException sample message");
        final ResponseEntity<?> actualOutput = underTest.handleIllegalArgumentException(illegalArgumentException);
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.getStatusCode());
        assertEquals(actualOutput.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}