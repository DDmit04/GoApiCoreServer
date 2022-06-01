package com.goapi.goapi.exception.database;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Database access not allowed")
public class DatabaseOwnerException extends RuntimeException {

    public DatabaseOwnerException(Integer databaseId) {
        super(String.format("You not allowed to access database with id = '%s'!", databaseId));
    }
}
