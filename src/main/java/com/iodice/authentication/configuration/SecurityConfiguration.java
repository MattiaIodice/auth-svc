package com.iodice.authentication.configuration;

import com.iodice.authentication.filter.JwtFilter;
import com.iodice.authentication.service.AuthUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for authentication and authorization. <br>
 * The AuthenticationManger leverages the Spring Security WebSecurityConfigurerAdapter logic.
 *
 * @author Mattia Iodice
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final AuthUserDetailsService authUserDetailsService;
    private final JwtFilter jwtFilter;

    /**
     * [Authorization] Add custom JWT filter to the chain of HTTP filters.
     *
     * @param http Http security
     * @throws Exception Due to inner exceptions
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/authentication/*")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * [Authentication] Assign the custom UserDetails to AuthenticationManager bean <br>
     * for calling "matches", taking the encoded password by DB through UserDetails.
     *
     * @param auth AuthenticationManger Builder of Spring Security
     * @throws Exception Due to inner exceptions
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authUserDetailsService);
    }

    /**
     * [Authentication] Create bean for AuthenticationManager. <br>
     * It is assigned to "Spring Security Default AuthenticationManager" (by superclass).
     *
     * @return AuthenticationManager Bean
     * @throws Exception Due to inner exceptions
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * [Authentication] Create bean for PasswordEncoder. <br>
     * "Spring Security Default Authentication Manager" requires a Password Encoder, <br>
     * because during authentication it is invoked PasswordEncoder "matches" taking <br>
     * decoded password by input (Dto) and encoded password by DB (throw UserDeatils).
     *
     * @return PasswordEncoder Bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
