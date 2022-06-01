package com.goapi.goapi.exception.userApi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "User api access not allowed")
public class UserApiOwnerException extends RuntimeException {

    public UserApiOwnerException(Integer userId, Integer userApiId) {
        super(String.format("User with id = '%s' isn't owner of user api with id = '%s'!", userApiId));
    }
}
