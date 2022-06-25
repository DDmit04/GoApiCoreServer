package com.goapi.goapi.exception.appService.database;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Database not found")
public class DatabaseNotFoundException extends RuntimeException {

    public DatabaseNotFoundException(Integer dbId) {
        super(String.format("Database with id = '%s' not found!", dbId));
    }
}
