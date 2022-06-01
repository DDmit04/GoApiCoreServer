package com.goapi.goapi.exception.userApi.request;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User api request argument not found in template!")
public class UserApiRequestArgumentMismatchException extends RuntimeException {

    public UserApiRequestArgumentMismatchException(String notMatchingArg) {

        super(String.format("User api request arg with name '%s' not found in template!", notMatchingArg));
    }
}
