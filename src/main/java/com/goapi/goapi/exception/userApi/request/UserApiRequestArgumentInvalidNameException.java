package com.goapi.goapi.exception.userApi.request;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Some of user api request arguments has invalid name!")
public class UserApiRequestArgumentInvalidNameException extends RuntimeException {

    public UserApiRequestArgumentInvalidNameException() {

        super("Some of user api requests argument has invalid name!");
    }
}
