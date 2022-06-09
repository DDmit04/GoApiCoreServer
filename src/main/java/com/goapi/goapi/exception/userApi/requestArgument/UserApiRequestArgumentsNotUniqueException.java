package com.goapi.goapi.exception.userApi.requestArgument;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User api request arguments are not unique!")
public class UserApiRequestArgumentsNotUniqueException extends RuntimeException {

    public UserApiRequestArgumentsNotUniqueException(List<String> arguments) {

        super(String.format("User api request args with names [%s] are not unique!", String.join(",", arguments)));
    }
}
