package com.goapi.goapi.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User not found!")
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Integer userId) {
        super(String.format("User with id = '%s' not found", userId));
    }
}
