package com.iodice.authentication.service;

import com.iodice.authentication.exception.AuthException;
import com.iodice.authentication.exception.RegisterException;
import com.iodice.authentication.model.document.AccountDocument;
import com.iodice.authentication.model.dto.AccountDto;
import com.iodice.authentication.model.dto.AuthenticationRequestDto;
import com.iodice.authentication.repository.AuthenticationRepository;
import lombok.NonNull;
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
     * Build an AccountDto performing the 'Register' steps:
     * <ul>
     *     <li>Check if an account does not exist for 'username'</li>
     *     <li>Encrypt 'plainPassword'</li>
     *     <li>Build and save account into DB</li>
     * </ul>
     *
     * @param authenticationRequestDto Dto with 'username' and 'plainPassword'
     * @return New user account
     */
    public AccountDto register(@NonNull final AuthenticationRequestDto authenticationRequestDto) throws AuthException {
        final String username = authenticationRequestDto.getUsername();
        if (username == null || username.isEmpty())
            throw new IllegalArgumentException("Input username must be specified.");

        final String plainPassword = authenticationRequestDto.getPlainPassword();
        if (plainPassword == null || plainPassword.isEmpty())
            throw new IllegalArgumentException("Input plain password must be specified.");

        if (authenticationRepository.existsByUsername(username))
            throw new RegisterException("An account with username [" + username + "] already exists.");

        log.debug("Register - Checked that the account for username [{}] does not exist yet", username);

        final String encryptedPassword = passwordEncoder.encode(plainPassword);
        final AccountDocument newAccountDocument = this.saveAccount(username, encryptedPassword);

        return new AccountDto(newAccountDocument.getUsername());
    }

    /**
     * Get an AccountDto performing the 'Login' steps:
     * <ul>
     *     <li>Authentication (by Spring "AuthenticationManager")</li>
     *     <ul>
     *         <li>Check if an account exists for 'username'</li>
     *         <li>Perform 'hashing comparison' between encoded 'plainPassword' hash and stored one</li>
     *     </ul>
     *     <li>JWT Generation</li>
     * </ul>
     * @param authenticationRequestDto Dto with 'username' and 'plainPassword'
     * @return Account with JWT
     */
    public AccountDto login(@NonNull final AuthenticationRequestDto authenticationRequestDto) {
        final String username = authenticationRequestDto.getUsername();
        if (username == null || username.isEmpty())
            throw new IllegalArgumentException("Input username must be specified.");

        final String plainPassword = authenticationRequestDto.getPlainPassword();
        if (plainPassword == null || plainPassword.isEmpty())
            throw new IllegalArgumentException("Input plain password must be specified.");

        authenticationManagerService.authenticate(new UsernamePasswordAuthenticationToken(username, plainPassword));
        log.debug("Login - The authentication by \"AuthenticationManager\" for username [{}] has been performed successfully", username);
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
