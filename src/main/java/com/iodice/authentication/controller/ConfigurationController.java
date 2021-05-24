package com.iodice.authentication.controller;

import com.iodice.authentication.exception.ConfigurationException;
import com.iodice.authentication.model.dto.ConfigurationRequestDto;
import com.iodice.authentication.model.dto.ConfigurationResponseDto;
import com.iodice.authentication.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Account configuration APIs.
 * @author Mattia Iodice
 * @version 1.0.0
 */
@RestController
@RequestMapping(path = "configuration/")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConfigurationController {
    private final ConfigurationService configurationService;

    /**
     * Get account configuration API.
     * @param username PathVariable - Username
     * @return Account configuration
     */
    @GetMapping(value = "{username}")
    public ResponseEntity<ConfigurationResponseDto> getConfiguration(
            @PathVariable(value = "username") final String username) throws ConfigurationException {
        final ConfigurationResponseDto configuration = configurationService.getConfiguration(username);
        return new ResponseEntity<>(configuration, HttpStatus.OK);
    }

    /**
     * Create an account configuration API.
     * @param configurationDocument Body - Account configuration (w/ username)
     * @return Created account configuration
     */
    @PostMapping()
    public ResponseEntity<ConfigurationResponseDto> saveConfiguration(
            @RequestBody final ConfigurationRequestDto configurationRequestDto) throws ConfigurationException {
        final ConfigurationResponseDto responseDto = configurationService.saveConfiguration(configurationRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
