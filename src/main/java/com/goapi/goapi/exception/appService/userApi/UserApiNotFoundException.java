package com.goapi.goapi.exception.appService.userApi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User api not found")
public class UserApiNotFoundException extends RuntimeException {

    public UserApiNotFoundException(Integer userApiId) {
        super(String.format("User api with id = '%s' not found!", userApiId));
    }
}
