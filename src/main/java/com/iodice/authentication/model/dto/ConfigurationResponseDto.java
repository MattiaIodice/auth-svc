package com.iodice.authentication.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ConfigurationResponseDto {
    private final String username;
    private final String description;
    private final String preferences;
}
