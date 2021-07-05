package com.iodice.authentication.controller;

import com.iodice.authentication.exception.LoginException;
import com.iodice.authentication.exception.RegisterException;
import com.iodice.authentication.model.dto.AccountDto;
import com.iodice.authentication.model.dto.AuthenticationRequestDto;
import com.iodice.authentication.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {
    private AuthenticationController underTest;
    private AuthenticationService authenticationServiceMocked;

    @BeforeEach
    public void setUp() {
        authenticationServiceMocked = mock(AuthenticationService.class);
        underTest = new AuthenticationController(authenticationServiceMocked);
    }

    @Test
    void registerShouldReturn2xxWhenInputIsValid() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto("dummyUsername", "dummyPwd");
        final AccountDto accountDto = new AccountDto("dummyUsername", "dummyToken");
        when(authenticationServiceMocked.register(any())).thenReturn(accountDto);

        final ResponseEntity<AccountDto> actualOutput = underTest.register(inputDto);

        verify(authenticationServiceMocked, times(1)).register(any());
        assertNotNull(actualOutput);
        assertEquals(actualOutput.getStatusCode(), HttpStatus.CREATED);
        assertSame(actualOutput.getBody(), accountDto);
    }

    @Test
    void registerShouldThrowAnExceptionWhenAnExceptionOccursInService() {
        when(authenticationServiceMocked.register(any())).thenThrow(RegisterException.class);

        assertThrows(RegisterException.class, () -> underTest.register(null));
    }

    @Test
    void loginShouldReturn2xxWhenInputIsValid() {
        final AuthenticationRequestDto inputDto = new AuthenticationRequestDto("dummyUsername", "dummyPwd");
        final AccountDto accountDto = new AccountDto("dummyUsername", "dummyToken");
        when(authenticationServiceMocked.login(any())).thenReturn(accountDto);

        final ResponseEntity<AccountDto> actualOutput = underTest.login(inputDto);

        verify(authenticationServiceMocked, times(1)).login(any());
        assertNotNull(actualOutput);
        assertEquals(actualOutput.getStatusCode(), HttpStatus.OK);
        assertSame(actualOutput.getBody(), accountDto);
    }

    @Test
    void loginShouldThrowAnExceptionWhenAnExceptionOccursInService() {
        when(authenticationServiceMocked.login(any())).thenThrow(LoginException.class);

        assertThrows(LoginException.class, () -> underTest.login(null));
    }
}