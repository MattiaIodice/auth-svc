package com.iodice.authentication.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler(RegisterExceptionCustom.class)
    public ResponseEntity<?> handleRegisterException(final RegisterExceptionCustom ex) {
        log.error("An exception occurred during register: ", ex);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LoginExceptionCustom.class)
    public ResponseEntity<?> handleLoginException(final LoginExceptionCustom ex) {
        log.error("An exception occurred during login: ", ex);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConfigurationException.class)
    public ResponseEntity<?> handleConfigurationException(final ConfigurationException ex) {
        log.error("An exception occurred during configuration: ", ex);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}