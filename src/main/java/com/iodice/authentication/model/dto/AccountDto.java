package com.iodice.authentication.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Response for register and login APIs.
 * @author Mattia Iodice
 * @version 1.0.0
 */
@Data
public class AccountDto implements Serializable {
    private final String username;
    private final String authenticationToken;

    public AccountDto(final String username) {
        this.username = username;
        this.authenticationToken = "";
    }

    public AccountDto(final String username, final String authenticationToken) {
        this.username = username;
        this.authenticationToken = authenticationToken;
    }
}
