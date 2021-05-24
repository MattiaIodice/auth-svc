package com.iodice.authentication.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Document class for configurations.
 * @author Mattia Iodice
 * @version 1.0.0
 */
@Document(collection = "configuration")
@Data
@AllArgsConstructor
public class ConfigurationDocument implements Serializable {

    @Id
    private final String username;

    private String description;

    private String preferences;
}
