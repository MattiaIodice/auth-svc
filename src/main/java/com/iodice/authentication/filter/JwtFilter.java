package com.iodice.authentication.filter;

import com.iodice.authentication.service.AuthUserDetailsService;
import com.iodice.authentication.service.JwtUtilService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Custom JWT Filter as part of filtering chain used for Authorization.
 * @author Mattia Iodice
 * @version 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtFilter extends OncePerRequestFilter {
    private final AuthUserDetailsService userDetailsService;
    private final JwtUtilService jwtUtilService;

    /**
     * It performs Authorization as a filter.
     * @param request The input Http request to filter
     * @param response The output Http response filtered
     * @param chain The chain of filters
     * @throws ServletException Due to inner exception
     * @throws IOException Due to inner exception
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String username = null;
        String jwt = null;

        // Get token and username
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtilService.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            log.debug("Dentro filtro");
            if (jwtUtilService.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Pass to the Spring Security Context the token
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        // No-blocking action, continue with other filters
        chain.doFilter(request, response);
    }
}