package com.goapi.goapi.exception.userApi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "User api requests count is max for current user api tariff")
public class UserApiRequestsCountCupException extends RuntimeException {

    public UserApiRequestsCountCupException(Integer userApiId) {
        super(String.format("User with id = '%s' can't add new api request, because reach max requests count for current user api tariff!", userApiId));
    }
}
