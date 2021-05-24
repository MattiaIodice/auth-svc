package com.iodice.authentication.service;

import com.iodice.authentication.exception.CustomAuthenticationException;
import com.iodice.authentication.exception.RegisterExceptionCustom;
import com.iodice.authentication.model.document.AccountDocument;
import com.iodice.authentication.model.dto.AccountDto;
import com.iodice.authentication.model.dto.AuthenticationRequestDto;
import com.iodice.authentication.repository.AuthenticationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Business logic of register and login APIs.
 * @author Mattia Iodice
 * @version 1.0.0
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationService {
    private final AuthenticationManager authenticationManagerService;
    private final JwtUtilService jwtUtilService;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationRepository authenticationRepository;

    /**
     * Register: Check if an account exists, encrypt plain password, and save account into DB.
     *
     * @param authenticationRequestDto Dto with username and plain password
     * @return The new user account with username created
     */
    public AccountDto register(final AuthenticationRequestDto authenticationRequestDto) throws CustomAuthenticationException {
        final String username = authenticationRequestDto.getUsername();
        final String plainPassword = authenticationRequestDto.getPlainPassword();

        if (authenticationRepository.existsByUsername(username))
            throw new RegisterExceptionCustom("An account with username [" + username + "] already exists.");

        final String encryptedPassword = passwordEncoder.encode(plainPassword);
        final AccountDocument newAccountDocument = this.saveAccount(username, encryptedPassword);

        return new AccountDto(newAccountDocument.getUsername());
    }

    /**
     * Login: Authenticate (who you are) and generate JWT (for next system APIs).
     *
     * @param authenticationRequestDto Username and plainPassword into a Dto
     * @return The result login object
     */
    public AccountDto login(final AuthenticationRequestDto authenticationRequestDto) {
        final String username = authenticationRequestDto.getUsername();
        final String plainPassword = authenticationRequestDto.getPlainPassword();

        authenticationManagerService.authenticate(new UsernamePasswordAuthenticationToken(username, plainPassword));
        log.debug("Login - The authentication (with \"AuthenticationManager\") for username [{}] has been performed successfully", username);
        final String token = jwtUtilService.generateToken(username);
        log.debug("Login - The JWT generation for username [{}] has been performed successfully", username);

        return new AccountDto(username, token);
    }

    private AccountDocument saveAccount(final String username, final String encryptedPassword) {
        AccountDocument newAccountDocument = AccountDocument.builder()
                .username(username)
                .password(encryptedPassword)
                .build();
        newAccountDocument = authenticationRepository.save(newAccountDocument);
        log.debug("Register - The account for username [{}] has been saved successfully", username);

        return newAccountDocument;
    }
}
