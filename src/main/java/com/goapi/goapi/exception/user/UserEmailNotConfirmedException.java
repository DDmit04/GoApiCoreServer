package com.goapi.goapi.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User email not confirmed!")
public class UserEmailNotConfirmedException extends RuntimeException {

    public UserEmailNotConfirmedException(Integer userId) {
        super(String.format("Email od user with id = '%s' is not confirmed", userId));
    }
}
