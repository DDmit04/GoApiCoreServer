package com.goapi.goapi.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Tokens not matching!")
public class TokenNotMatchingException extends RuntimeException {

    public TokenNotMatchingException(Integer userId, String token) {
        super(String.format("User with id = '%s' trying use invalid token = ''%s", userId, token));
    }
}
