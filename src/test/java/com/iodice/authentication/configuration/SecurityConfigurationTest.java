package com.iodice.authentication.configuration;

import com.iodice.authentication.filter.JwtFilter;
import com.iodice.authentication.service.AuthUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@Disabled
class SecurityConfigurationTest {
    private SecurityConfiguration underTest;
    private AuthUserDetailsService authUserDetailsService;

    @BeforeEach
    public void setUp() {
        authUserDetailsService = mock(AuthUserDetailsService.class);
        final JwtFilter jwtFilter = mock(JwtFilter.class);
        underTest = new SecurityConfiguration(authUserDetailsService, jwtFilter);
    }

    @Test
    void configureShouldDelegateToUserDetailsService() throws Exception {
        ObjectPostProcessor<Object> prova = new ObjectPostProcessor<Object>() {
            @Override
            public <O> O postProcess(O o) {
                return null;
            }
        };
        AuthenticationManagerBuilder auth = new AuthenticationManagerBuilder(prova);
        underTest.configure(auth);
        verify(auth, times(1)).userDetailsService(authUserDetailsService);
    }

    @Test
    void authenticationManagerBeanShouldReturnAuthenticationManagerFromSuperclass() throws Exception {
        AuthenticationManager actualOutput = underTest.authenticationManagerBean();
        assertNotNull(actualOutput);
    }

    @Test
    void passwordEncoderShouldReturnABCryptPasswordEncoder() {
        PasswordEncoder actualOutput = underTest.passwordEncoder();
        assertNotNull(actualOutput);
    }
}