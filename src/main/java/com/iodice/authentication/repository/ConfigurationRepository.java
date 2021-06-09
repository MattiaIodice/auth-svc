package com.iodice.authentication.repository;


import com.iodice.authentication.model.document.ConfigurationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Mongo Repository for configuration documents.
 * @author Mattia Iodice
 * @version 1.0.0
 */
public interface ConfigurationRepository extends MongoRepository<ConfigurationDocument, String> {

}
