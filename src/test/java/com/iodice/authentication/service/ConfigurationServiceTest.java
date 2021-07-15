package com.iodice.authentication.service;

import com.iodice.authentication.model.document.ConfigurationDocument;
import com.iodice.authentication.model.dto.ConfigurationRequestDto;
import com.iodice.authentication.model.dto.ConfigurationResponseDto;
import com.iodice.authentication.repository.ConfigurationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfigurationServiceTest {
    private ConfigurationService underTest;
    private ConfigurationRepository configurationRepository;
    private ConfigurationDocument configurationSample;

    @BeforeEach
    public void setUp() {
        configurationRepository = mock(ConfigurationRepository.class);
        configurationSample = new ConfigurationDocument("uExample", "dExample", "pExample");
        when(configurationRepository.save(any())).thenReturn(configurationSample);

        underTest = new ConfigurationService(configurationRepository);
    }

    @Test
    void getConfigurationShouldReturnAConfigurationWhenInputIsValid() {
        final String username = "UsernameExample";
        final Optional<ConfigurationDocument> documentOptional = Optional.of(
                new ConfigurationDocument(username, "DescriptionExample", "PreferencesExample")
        );
        when(configurationRepository.findById(username)).thenReturn(documentOptional);

        final ConfigurationResponseDto actualOutput = underTest.getConfiguration(username);

        verify(configurationRepository, times(1)).findById(username);
        assertNotNull(actualOutput);
        assertEquals(actualOutput.getUsername(), documentOptional.get().getUsername());
        assertEquals(actualOutput.getPreferences(), documentOptional.get().getPreferences());
        assertEquals(actualOutput.getDescription(), documentOptional.get().getDescription());
    }

    @Test
    void getConfigurationShouldThrowAnExceptionWhenUsernameIsEmpty() {
        final String username = "";
        assertThrows(IllegalArgumentException.class,  () -> underTest.getConfiguration(username));
    }

    @Test
    void saveConfigurationShouldReturnANewConfigurationWhenInputIsValid() {
        final ConfigurationRequestDto inputDto = new ConfigurationRequestDto("usernameExample", "descriptionExample", "preferencesExample");

        final ConfigurationResponseDto actualOutput = underTest.saveConfiguration(inputDto);

        assertNotNull(actualOutput);
        assertEquals(actualOutput.getUsername(), configurationSample.getUsername());
        assertEquals(actualOutput.getDescription(), configurationSample.getDescription());
        assertEquals(actualOutput.getPreferences(), configurationSample.getPreferences());
    }

    @Test
    void saveConfigurationShouldThrowAnExceptionWhenUsernameIsEmpty() {
        final ConfigurationRequestDto inputDto = new ConfigurationRequestDto("", "descriptionExample", "preferencesExample");

        assertThrows(IllegalArgumentException.class, () -> underTest.saveConfiguration(inputDto));
    }
}