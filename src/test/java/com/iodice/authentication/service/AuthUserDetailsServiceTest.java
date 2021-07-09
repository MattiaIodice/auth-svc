package com.iodice.authentication.service;

import com.iodice.authentication.model.document.AccountDocument;
import com.iodice.authentication.repository.AuthenticationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthUserDetailsServiceTest {
    private AuthUserDetailsService underTest;
    private AuthenticationRepository authenticationRepository;

    private String username;

    @BeforeEach
    public void setUp() {
        authenticationRepository = mock(AuthenticationRepository.class);
        underTest = new AuthUserDetailsService(authenticationRepository);
        username = "UsernameExample";
    }

    @Test
    void loadUserByUsernameShouldReturnACorrectUserWhenAnAccountExistsByUsername() {
        final Optional<AccountDocument> user = Optional.of(
                AccountDocument.builder()
                        .username("dummyUsername")
                        .password("dummyPassword")
                        .build()
        );
        when(authenticationRepository.findByUsername(username)).thenReturn(user);
        UserDetails actualOutput = underTest.loadUserByUsername(username);
        verify(authenticationRepository, times(1)).findByUsername(username);
        assertNotNull(actualOutput);
        assertEquals(actualOutput.getUsername(), username);
        assertEquals(actualOutput.getPassword(), user.get().getPassword());
    }

    @Test
    void loadUserByUsernameShouldThrowAnExceptionWhenAnAccountDoesNotExistByUsername() {
        when(authenticationRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> underTest.loadUserByUsername(username));
        verify(authenticationRepository, times(1)).findByUsername(any());
    }
}