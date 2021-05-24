package com.iodice.authentication.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConfigurationRequestDto implements Serializable {
    private final String username;
    private String description;
    private String preferences;
}
