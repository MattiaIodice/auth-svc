package com.iodice.authentication.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Request Dto for register and login APIs.
 * @author Mattia Iodice
 * @version 1.0.0
 */
@Data
public class AuthenticationRequestDto implements Serializable {
    private final String username;
    private final String plainPassword;
}
