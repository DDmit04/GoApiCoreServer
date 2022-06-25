package com.goapi.goapi.exception.appService.userApi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "User apis count is max for any user")
public class UserApisCountCupException extends RuntimeException {

    public UserApisCountCupException(Integer userId) {
        super(String.format("User with id = '%s' can't add new api, because reach max apis count for any user!", userId));
    }
}
