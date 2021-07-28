package com.iodice.authentication.service;

import com.iodice.authentication.model.exception.RegisterException;
import com.iodice.authentication.model.document.AccountDocument;
import com.iodice.authentication.model.dto.AccountDto;
import com.iodice.authentication.model.dto.AuthenticationRequestDto;
import com.iodice.authentication.repository.AuthenticationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {
    private AuthenticationService underTest;

    private AuthenticationRepository authenticationRepository;

    private AccountDocument accountDocumentMocked;
    private String tokenMocked;

    @BeforeEach
    public void setUp() {
        // Initialize underTest
        final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        final JwtUtilService jwtUtilService = mock(JwtUtilService.class);
        final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        authenticationRepository = mock(AuthenticationRepository.class);
        underTest = new AuthenticationService(authenticationManager, jwtUtilService, passwordEncoder, authenticationRepository);

        // Mock inner behaviours
        final String encryptedPasswordMocked = "dummyEncryptedPwd";
        when(passwordEncoder.encode(any())).thenReturn(encryptedPasswordMocked);

        accountDocumentMocked = AccountDocument.builder()
                .username("dummyUsername")
                .password(encryptedPasswordMocked)
                .build();
        when(authenticationRepository.save(any())).thenReturn(accountDocumentMocked);

        final Authentication authenticateMocked = new UsernamePasswordAuthenticationToken("UsernameExample", "PwdExample");
        when(authenticationManager.authenticate(any())).thenReturn(authenticateMocked);

        tokenMocked = "TokenExample";
        when(jwtUtilService.generateToken(any())).thenReturn(tokenMocked);
    }

    @Test
    void registerShouldReturnACorrectAccountWhenInputIsValid() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto("UsernameExample", "PwdExample");
        when(authenticationRepository.existsByUsername(any())).thenReturn(false);

        final AccountDto actualOutput = underTest.register(inputDto);

        assertNotNull(actualOutput);
        assertEquals(actualOutput.getUsername(), accountDocumentMocked.getUsername());
        assertEquals(actualOutput.getAuthenticationToken(), "");
    }

    @Test
    void registerShouldReturnAnIllegaleArgumentExceptionWhenUsernameIsNull() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto(null, "PwdExample");

        assertThrows(IllegalArgumentException.class, () -> underTest.register(inputDto));
    }

    @Test
    void registerShouldReturnAnIllegalArgumentExceptionWhenUsernameIsEmpty() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto("", "PwdExample");

        assertThrows(IllegalArgumentException.class, () -> underTest.register(inputDto));
    }

    @Test
    void registerShouldReturnAnIllegalArgumentExceptionWhenPwdIsNull() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto("UsernameExample", null);

        assertThrows(IllegalArgumentException.class, () -> underTest.register(inputDto));
    }

    @Test
    void registerShouldReturnAnIllegalArgumentExceptionWhenPwdIsEmpty() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto("UsernameExample", "");

        assertThrows(IllegalArgumentException.class, () -> underTest.register(inputDto));
    }

    @Test
    void registerShouldReturnAnExceptionWhenAccountAlreadyExist() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto("UsernameExample", "PwdExample");
        when(authenticationRepository.existsByUsername(any())).thenReturn(true);

        assertThrows(RegisterException.class, () -> underTest.register(inputDto));
    }

    @Test
    void loginShouldReturnACorrectAccountWhenInputIsValid() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto("UsernameExample", "PwdExample");

        final AccountDto actualOutput = underTest.login(inputDto);

        assertNotNull(actualOutput);
        assertEquals(actualOutput.getUsername(), inputDto.getUsername());
        assertEquals(actualOutput.getAuthenticationToken(), tokenMocked);
    }

    @Test
    void loginShouldReturnAnExceptionWhenUsernameIsNull() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto(null, "PwdExample");

        assertThrows(IllegalArgumentException.class, () -> underTest.login(inputDto));
    }

    @Test
    void loginShouldReturnAnExceptionWhenUsernameIsEmpty() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto("", "PwdExample");

        assertThrows(IllegalArgumentException.class, () -> underTest.login(inputDto));
    }

    @Test
    void loginShouldReturnAnExceptionWhenPwdIsNull() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto("UsernameExample", null);

        assertThrows(IllegalArgumentException.class, () -> underTest.login(inputDto));
    }

    @Test
    void loginShouldReturnAnExceptionWhenPwdIsEmpty() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto("UsernameExample", "");

        assertThrows(IllegalArgumentException.class, () -> underTest.login(inputDto));
    }
}