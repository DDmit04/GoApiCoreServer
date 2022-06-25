package com.goapi.goapi.exception.appService.userApi.request;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED, reason = "User api request not accepting this http method!")
public class UserApiRequestMethodException extends RuntimeException {
    public UserApiRequestMethodException(Integer requestId, String method) {
        super(String.format("User api request with id = '%s' not accepting http method = '%s'", requestId, method));
    }
}
