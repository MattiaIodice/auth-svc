package com.iodice.authentication.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(final IllegalArgumentException ex) {
        log.error("A bad request from client: ", ex);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegisterException.class)
    public ResponseEntity<?> handleRegisterException(final RegisterException ex) {
        log.error("An exception occurred during register: ", ex);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<?> handleLoginException(final LoginException ex) {
        log.error("An exception occurred during login: ", ex);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConfigurationException.class)
    public ResponseEntity<?> handleConfigurationException(final ConfigurationException ex) {
        log.error("An exception occurred during configuration: ", ex);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
