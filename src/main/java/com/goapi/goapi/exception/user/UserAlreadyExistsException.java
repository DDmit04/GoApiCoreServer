package com.goapi.goapi.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "User already exists!")
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String username, String email) {
        super(String.format("User with username = '%s' or email = '%s' already exists!", username, email));
    }
}
