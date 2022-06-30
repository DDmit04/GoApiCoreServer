package com.goapi.goapi.exception.appService.database;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.GONE, reason = "Database is disabled")
public class DatabaseDisabledException extends RuntimeException {

    public DatabaseDisabledException(Integer dbId) {
        super(String.format("Database with id = '%s' is disabled!", dbId));
    }
}
