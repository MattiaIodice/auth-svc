package com.iodice.authentication.service;

import com.iodice.authentication.exception.RegisterException;
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

    private AuthenticationManager authenticationManager;
    private JwtUtilService jwtUtilService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationRepository authenticationRepository;

    private String encryptedPasswordMocked;
    private AccountDocument accountDocumentMocked;
    private Authentication authenticate;

    @BeforeEach
    public void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        jwtUtilService = mock(JwtUtilService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authenticationRepository = mock(AuthenticationRepository.class);
        underTest = new AuthenticationService(authenticationManager, jwtUtilService, passwordEncoder, authenticationRepository);
        encryptedPasswordMocked = "dummyEncryptedPwd";
        accountDocumentMocked = AccountDocument.builder()
                .username("dummyUsername")
                .password(encryptedPasswordMocked)
                .build();
        authenticate = new UsernamePasswordAuthenticationToken("dummyUsername", "dummyPwd");
    }

    @Test
    void register_ItShouldReturnACorrectResponseWhenInputIsValid() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto("dummyUsername", "dummyPwd");
        when(passwordEncoder.encode(any())).thenReturn(encryptedPasswordMocked);
        when(authenticationRepository.existsByUsername(any())).thenReturn(false);
        when(authenticationRepository.save(any())).thenReturn(accountDocumentMocked);

        final AccountDto actualOutput = underTest.register(inputDto);

        assertNotNull(actualOutput);
        assertEquals(actualOutput.getUsername(), accountDocumentMocked.getUsername());
        assertEquals(actualOutput.getAuthenticationToken(), "");
    }

    @Test
    void register_ItShouldReturnAnExceptionWhenUsernameIsEmpty() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto("", "dummyPwd");
        when(passwordEncoder.encode(any())).thenReturn(encryptedPasswordMocked);
        when(authenticationRepository.existsByUsername(any())).thenReturn(false);
        when(authenticationRepository.save(any())).thenReturn(accountDocumentMocked);

        assertThrows(IllegalArgumentException.class, () -> underTest.register(inputDto));
    }

    @Test
    void register_ItShouldReturnAnExceptionWhenPwdIsEmpty() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto("dummyUsername", "");
        when(passwordEncoder.encode(any())).thenReturn(encryptedPasswordMocked);
        when(authenticationRepository.existsByUsername(any())).thenReturn(false);
        when(authenticationRepository.save(any())).thenReturn(accountDocumentMocked);

        assertThrows(IllegalArgumentException.class, () -> underTest.register(inputDto));
    }

    @Test
    void register_ItShouldReturnAnExceptionWhenAccountAlreadyExist() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto("dummyUsername", "dummyPwd");
        when(passwordEncoder.encode(any())).thenReturn(encryptedPasswordMocked);
        when(authenticationRepository.existsByUsername(any())).thenReturn(true);
        when(authenticationRepository.save(any())).thenReturn(accountDocumentMocked);

        assertThrows(RegisterException.class, () -> underTest.register(inputDto));
    }

    @Test
    void login_ItShouldReturnACorrectResponseWhenInputIsValid() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto("dummyUsername", "dummyPwd");
        when(authenticationManager.authenticate(any())).thenReturn(authenticate);
        final String token = "dummyToken";
        when(jwtUtilService.generateToken(any())).thenReturn(token);

        final AccountDto actualOutput = underTest.login(inputDto);

        assertNotNull(actualOutput);
        assertEquals(actualOutput.getUsername(), inputDto.getUsername());
        assertEquals(actualOutput.getAuthenticationToken(), token);
    }

    @Test
    void login_ItShouldReturnAnExceptionWhenUsernameIsEmpty() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto("", "dummyPwd");
        when(authenticationManager.authenticate(any())).thenReturn(authenticate);
        final String token = "dummyToken";
        when(jwtUtilService.generateToken(any())).thenReturn(token);

        assertThrows(IllegalArgumentException.class, () -> underTest.login(inputDto));
    }

    @Test
    void login_ItShouldReturnAnExceptionWhenPwdIsEmpty() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto("dummyUsername", "");
        when(authenticationManager.authenticate(any())).thenReturn(authenticate);
        final String token = "dummyToken";
        when(jwtUtilService.generateToken(any())).thenReturn(token);

        assertThrows(IllegalArgumentException.class, () -> underTest.login(inputDto));
    }
}