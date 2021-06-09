package com.iodice.authentication.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class CustomAuthenticationException extends RuntimeException {
    private final Throwable cause;
    private final String message;

    public CustomAuthenticationException(final String message) {
        this.cause = null;
        this.message = message;
    }
}
