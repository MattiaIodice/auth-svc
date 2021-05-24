package com.iodice.authentication.service;

import com.iodice.authentication.exception.ConfigurationException;
import com.iodice.authentication.model.document.ConfigurationDocument;
import com.iodice.authentication.model.dto.ConfigurationRequestDto;
import com.iodice.authentication.model.dto.ConfigurationResponseDto;
import com.iodice.authentication.repository.ConfigurationRepository;
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
    public ConfigurationResponseDto getConfiguration(final String username) throws ConfigurationException {
        final Optional<ConfigurationDocument> documentOptional = configurationRepository.findById(username);

        if (!documentOptional.isPresent()) {
            throw new ConfigurationException("There is no document for username [" + username + "]");
        }

        final ConfigurationDocument document = documentOptional.get();

        return new ConfigurationResponseDto(username, document.getDescription(), document.getPreferences());
    }

    /**
     * Save account configuration.
     * @param configurationRequestDto Account configuration
     * @return The created configuration of the user
     */
    public ConfigurationResponseDto saveConfiguration(final ConfigurationRequestDto configurationRequestDto)
            throws ConfigurationException {
        final String username = configurationRequestDto.getUsername();

        if (!Optional.ofNullable(username).isPresent()) {
            throw new ConfigurationException("No username in the request body");
        }

        /*if (!username.equals(configurationDocument.getUsername())) {
            throw new ConfigurationException("The configuration is not related to the input user reading the jwt");
        }*/
        ConfigurationDocument document = new ConfigurationDocument(username,
                configurationRequestDto.getDescription(), configurationRequestDto.getPreferences());
        document = configurationRepository.save(document);

        return new ConfigurationResponseDto(username, document.getDescription(), document.getPreferences());
    }
}
