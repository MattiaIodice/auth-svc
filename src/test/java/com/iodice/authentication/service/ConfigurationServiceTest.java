package com.iodice.authentication.service;

import com.iodice.authentication.model.exception.ConfigurationException;
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
    void getConfigurationShouldReturnACorrectConfigurationWhenInputIsValid() {
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
    void getConfigurationShouldThrowAnIllegalArgumentExceptionWhenUsernameIsEmpty() {
        final String username = "";
        assertThrows(IllegalArgumentException.class,  () -> underTest.getConfiguration(username));
    }

    @Test
    void getConfigurationShouldThrowAnIllegalArgumentExceptionWhenUsernameIsNull() {
        final String username = null;
        assertThrows(IllegalArgumentException.class,  () -> underTest.getConfiguration(username));
    }

    @Test
    void getConfigurationShouldThrowAConfigurationExceptionWhenConfigurationNotFound() {
        final String username = "UsernameExample";
        when(configurationRepository.findById(username)).thenReturn(Optional.empty());
        assertThrows(ConfigurationException.class,  () -> underTest.getConfiguration(username));
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
    void saveConfigurationShouldThrowAnIllegalArgumentExceptionWhenUsernameIsNull() {
        final ConfigurationRequestDto inputDto = new ConfigurationRequestDto(null, "descriptionExample", "preferencesExample");

        assertThrows(IllegalArgumentException.class, () -> underTest.saveConfiguration(inputDto));
    }

    @Test
    void saveConfigurationShouldThrowAnIllegalArgumentExceptionWhenUsernameIsEmpty() {
        final ConfigurationRequestDto inputDto = new ConfigurationRequestDto("", "descriptionExample", "preferencesExample");

        assertThrows(IllegalArgumentException.class, () -> underTest.saveConfiguration(inputDto));
    }
}