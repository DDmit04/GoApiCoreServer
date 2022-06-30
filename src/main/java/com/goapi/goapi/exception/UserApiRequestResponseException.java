package com.goapi.goapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error while processing user api request!")
public class UserApiRequestResponseException extends RuntimeException {

    public UserApiRequestResponseException(Integer dbId, String query) {
        super(String.format("Error while recive response from user api request to database with id = '%s' query = '%s'", dbId, query));
    }
}
