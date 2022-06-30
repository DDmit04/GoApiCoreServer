package com.goapi.goapi.exception.appService.userApi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.GONE, reason = "User api is disabled")
public class UserApiDisabledException extends RuntimeException {

    public UserApiDisabledException(Integer dbId) {
        super(String.format("User api with id = '%s' is disabled!", dbId));
    }
}
