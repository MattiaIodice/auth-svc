package com.iodice.authentication.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConfigurationRequestDto implements Serializable {
    private final String username;
    private final String description;
    private final String preferences;
}
