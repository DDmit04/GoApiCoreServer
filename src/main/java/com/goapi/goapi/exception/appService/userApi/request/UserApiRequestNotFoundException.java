package com.goapi.goapi.exception.appService.userApi.request;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User api request not found")
public class UserApiRequestNotFoundException extends RuntimeException {

    public UserApiRequestNotFoundException(Integer userApiRequestId) {
        super(String.format("User api request with id = '%s' not found!", userApiRequestId));
    }

    public UserApiRequestNotFoundException(Integer apiId, Integer userApiRequestId) {
        super(String.format("User api request with id = '%s' in api with id = '%s' not found!", userApiRequestId, apiId));
    }
}
