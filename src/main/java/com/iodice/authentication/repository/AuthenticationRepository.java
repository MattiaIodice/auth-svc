package com.iodice.authentication.repository;

import com.iodice.authentication.model.document.AccountDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * MongoDB Repository for accounts.
 * @author Mattia Iodice
 * @version 1.0.0
 */
@Repository
public interface AuthenticationRepository extends MongoRepository<AccountDocument, String> {
    boolean existsByUsername(String username);

    Optional<AccountDocument> findByUsername(String username);
}
