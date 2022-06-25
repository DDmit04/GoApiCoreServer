package com.goapi.goapi.exception.appService.userApi.request;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "User api request key is invalid!")
public class UserApiRequestKeyException extends RuntimeException {
    public UserApiRequestKeyException(Integer requestId) {
        super(String.format("User api request with id = '%s' called with invalid key", requestId));
    }
}
