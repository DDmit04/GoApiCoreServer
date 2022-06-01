package com.goapi.goapi.exception.userApi.request;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "User api request processing error!")
public class UserApiRequestProcessingException extends RuntimeException {
    public UserApiRequestProcessingException(Integer requestId) {
        super(String.format("Error while processing user api request with id = '%s'", requestId));
    }
}
