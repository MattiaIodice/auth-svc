package com.iodice.authentication.controller;

import com.iodice.authentication.exception.ConfigurationException;
import com.iodice.authentication.model.dto.ConfigurationRequestDto;
import com.iodice.authentication.model.dto.ConfigurationResponseDto;
import com.iodice.authentication.service.ConfigurationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConfigurationControllerTest {
    private ConfigurationController underTest;
    private ConfigurationService configurationService;

    @BeforeEach
    public void setUp() {
        configurationService = mock(ConfigurationService.class);
        underTest = new ConfigurationController(configurationService);
    }

    @Test
    void getConfigurationShouldReturn2xxWhenInputIsValid() {
        final String username = "dummyUsername";
        final ConfigurationResponseDto configurationResponseMockedDto = new ConfigurationResponseDto("dummyUsername", "dummyDescription", "dummyPreferences");
        when(configurationService.getConfiguration(any())).thenReturn(configurationResponseMockedDto);

        ResponseEntity<ConfigurationResponseDto> actualOutput = underTest.getConfiguration(username);

        verify(configurationService, times(1)).getConfiguration(any());
        assertNotNull(actualOutput);
        assertSame(actualOutput.getBody(), configurationResponseMockedDto);
        assertEquals(actualOutput.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getConfigurationShouldThrowAnExceptionWhenAnExceptionOccursInService() {
        when(configurationService.getConfiguration(any())).thenThrow(ConfigurationException.class);

        assertThrows(ConfigurationException.class, () -> underTest.getConfiguration(null));
    }

    @Test
    void saveConfigurationShouldReturn2xxWhenInputIsValid() {
        final ConfigurationRequestDto inputDto = new ConfigurationRequestDto("dummyUsername", "dummyDescription", "dummyPreferences");
        final ConfigurationResponseDto configurationResponseMockedDto = new ConfigurationResponseDto("dummyUsername", "dummyDescription", "dummyPreferences");
        when(configurationService.saveConfiguration(any())).thenReturn(configurationResponseMockedDto);

        ResponseEntity<ConfigurationResponseDto> actualOutput = underTest.saveConfiguration(inputDto);

        verify(configurationService, times(1)).saveConfiguration(any());
        assertNotNull(actualOutput);
        assertSame(actualOutput.getBody(), configurationResponseMockedDto);
        assertEquals(actualOutput.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    void saveConfigurationShouldThrowAnExceptionWhenAnExceptionOccursInService() {
        when(configurationService.saveConfiguration(any())).thenThrow(ConfigurationException.class);

        assertThrows(ConfigurationException.class, () -> underTest.saveConfiguration(null));
    }
}