package com.iodice.authentication.filter;

import com.iodice.authentication.service.AuthUserDetailsService;
import com.iodice.authentication.service.JwtUtilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JwtFilterTest {
    private JwtFilter underTest;
    private AuthUserDetailsService userDetailsService;
    private JwtUtilService jwtUtilService;

    @BeforeEach
    public void setUp() {
        userDetailsService = mock(AuthUserDetailsService.class);
        jwtUtilService = mock(JwtUtilService.class);
        underTest = new JwtFilter(userDetailsService, jwtUtilService);
    }

    @Test
    void doFilterInternalShouldReturnASecurityContextWithAuthenticationToken() throws ServletException, IOException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = new MockHttpServletResponse();
        final FilterChain chain = new MockFilterChain();
        final String headerExample = "Bearer TokenExample";
        final String usernameExample = "usernameExample";
        final Collection<GrantedAuthority> authoritiesExample = new ArrayList<>();
        authoritiesExample.add(new SimpleGrantedAuthority("ADMIN"));
        final User userExample = new User("usernameExample", "pwdExample", authoritiesExample);
        when(request.getHeader(any())).thenReturn(headerExample);
        when(jwtUtilService.extractUsername(any())).thenReturn(usernameExample);
        when(jwtUtilService.validateToken(any(), any())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(any())).thenReturn(userExample);
        SecurityContextHolder.getContext().setAuthentication(null);

        underTest.doFilterInternal(request, response, chain);
        Authentication actualOutput = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(actualOutput);
        assertSame(actualOutput.getPrincipal(), userExample);
        assertArrayEquals(actualOutput.getAuthorities().toArray(), userExample.getAuthorities().toArray());
    }
}