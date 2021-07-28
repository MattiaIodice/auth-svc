package com.iodice.authentication.model.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ConfigurationException extends RuntimeException {
    private final Throwable cause;
    private final String message;

    public ConfigurationException(final String message) {
        this.cause = null;
        this.message = message;
    }
}
