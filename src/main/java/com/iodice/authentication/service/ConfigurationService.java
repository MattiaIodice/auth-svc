package com.iodice.authentication.service;

import com.iodice.authentication.exception.ConfigurationException;
import com.iodice.authentication.model.document.ConfigurationDocument;
import com.iodice.authentication.model.dto.ConfigurationRequestDto;
import com.iodice.authentication.model.dto.ConfigurationResponseDto;
import com.iodice.authentication.repository.ConfigurationRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Configuration (w/ authorization) logic.
 * @author Mattia Iodice
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConfigurationService {
    private final ConfigurationRepository configurationRepository;

    /**
     * Get account configuration by username.
     * @param username Username
     * @return Account configuration
     */
    public ConfigurationResponseDto getConfiguration(@NonNull final String username) throws ConfigurationException {
        if (username.isEmpty())
            throw new IllegalArgumentException("Username cannot be empty");

        final Optional<ConfigurationDocument> documentOptional = configurationRepository.findById(username);
        if (!documentOptional.isPresent()) {
            throw new ConfigurationException("There is no configuration for username [" + username + "]");
        }

        final ConfigurationDocument configuration = documentOptional.get();

        return new ConfigurationResponseDto(username, configuration.getDescription(), configuration.getPreferences());
    }

    /**
     * Save account configuration.
     * @param configurationRequestDto Account configuration
     * @return The created configuration of the user
     */
    public ConfigurationResponseDto saveConfiguration(@NonNull final ConfigurationRequestDto configurationRequestDto)
            throws ConfigurationException {
        final String username = configurationRequestDto.getUsername();
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("No username passed in the request body");
        }

        ConfigurationDocument configuration = new ConfigurationDocument(username,
                configurationRequestDto.getDescription(), configurationRequestDto.getPreferences());
        configuration = configurationRepository.save(configuration);

        return new ConfigurationResponseDto(configuration.getUsername(), configuration.getDescription(), configuration.getPreferences());
    }
}
