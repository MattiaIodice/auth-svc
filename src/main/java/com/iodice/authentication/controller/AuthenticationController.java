package com.iodice.authentication.controller;

import com.iodice.authentication.exception.LoginException;
import com.iodice.authentication.exception.RegisterException;
import com.iodice.authentication.model.dto.AccountDto;
import com.iodice.authentication.model.dto.AuthenticationRequestDto;
import com.iodice.authentication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * It exposes register and login APIs.
 * @author Mattia Iodice
 * @version 1.0.0
 */
@RestController
@RequestMapping(path = "authentication/")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    /**
     * The API for account creation.
     *
     * @param authenticationRequestDto Username and password to register
     * @return The new account
     */
    @PostMapping(value = "register")
    public ResponseEntity<AccountDto> register(@RequestBody final AuthenticationRequestDto authenticationRequestDto)
            throws RegisterException {
        final AccountDto createdAccountDto = authenticationService.register(authenticationRequestDto);
        log.debug("Register - The API with body request [{}] has been consumed successfully", authenticationRequestDto);
        return new ResponseEntity<>(createdAccountDto, HttpStatus.CREATED);
    }

    /**
     * The API for login.
     *
     * @param authenticationRequestDto Account credentials (username and password)
     * @return The result Dto with a valid authorization token
     */
    @PostMapping(value = "login")
    public ResponseEntity<AccountDto> login(@RequestBody final AuthenticationRequestDto authenticationRequestDto)
        throws LoginException {
        final AccountDto resultAccountDto = authenticationService.login(authenticationRequestDto);
        log.debug("Login - The API with body request [{}] has been consumed successfully", authenticationRequestDto);
        return new ResponseEntity<>(resultAccountDto, HttpStatus.OK);
    }
}
