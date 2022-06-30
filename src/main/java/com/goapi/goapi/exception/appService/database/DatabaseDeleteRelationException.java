package com.goapi.goapi.exception.appService.database;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Database can't be deleted because one or more user apis depends on it!")
public class DatabaseDeleteRelationException extends RuntimeException {

    public DatabaseDeleteRelationException(Integer dbId) {
        super(String.format("Database with id = '%s' can't be deleted because user api!", dbId));
    }
}
