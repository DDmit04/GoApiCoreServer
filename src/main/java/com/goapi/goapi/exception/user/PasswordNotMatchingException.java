package com.goapi.goapi.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "User password is invalid!")
public class PasswordNotMatchingException extends RuntimeException {

    public PasswordNotMatchingException(Integer userId) {
        super(String.format("User with id = '%s' trying use invalid password", userId));
    }
}
