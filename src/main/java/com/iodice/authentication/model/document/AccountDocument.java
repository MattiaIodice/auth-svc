package com.iodice.authentication.model.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Account document class with two indexes (username and usernmane-password).
 * @author Mattia Iodice
 * @version 1.0.0
 */
@Document(collection = "account")
@Data
@Builder
@CompoundIndexes({
        @CompoundIndex(name = "username_password_index", def = "{'username' : 1, 'password': 1}")
})
public class AccountDocument {
    @Id
    private final String accountId;

    @Indexed
    private final String username;

    private final String password;
}
