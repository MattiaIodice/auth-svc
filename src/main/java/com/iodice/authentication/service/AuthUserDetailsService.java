package com.iodice.authentication.service;

import com.iodice.authentication.model.document.AccountDocument;
import com.iodice.authentication.repository.AuthenticationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Implementation of Spring Security User Details using MongoDB Repository
 * @author Mattia Iodice
 * @version 1.0.0
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Profile("!test")
public class AuthUserDetailsService implements UserDetailsService {
    private final AuthenticationRepository authenticationRepository;

    /**
     * Load a user in the context, it is required for validate the authentication
     * @param username Username of an account
     * @return Instance of User for Spring Security
     * @throws UsernameNotFoundException Account not found exception
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final AccountDocument account = authenticationRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found for username: " + username));

        return new User(username, account.getPassword(), Collections.emptyList());
    }
}