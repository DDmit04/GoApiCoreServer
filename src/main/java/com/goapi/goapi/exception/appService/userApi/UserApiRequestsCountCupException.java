package com.goapi.goapi.exception.appService.userApi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "User api requests count is max for any user")
public class UserApiRequestsCountCupException extends RuntimeException {

    public UserApiRequestsCountCupException(Integer userApiId) {
        super(String.format("Can't add new api request to api with id = '%s', because reach max requests count for any user!", userApiId));
    }
}
