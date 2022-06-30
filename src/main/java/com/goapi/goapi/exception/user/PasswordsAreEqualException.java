package com.goapi.goapi.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Old and new passwords are equal!")
public class PasswordsAreEqualException extends RuntimeException {

    public PasswordsAreEqualException(Integer userId) {
        super(String.format("User with id = '%s' trying change password but new password is equal to old!", userId));
    }
}
