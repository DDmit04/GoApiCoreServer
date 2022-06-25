package com.goapi.goapi.exception.appService.userApi.requestArgument;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Error while updating user api request!")
public class UserApiRequestArgumentUpdateException extends RuntimeException {

    public UserApiRequestArgumentUpdateException(Integer requestId, Integer argId) {

        super(String.format("Error while updating user api request with id = '%s' and argument id = '%s'", requestId, argId));
    }
}
