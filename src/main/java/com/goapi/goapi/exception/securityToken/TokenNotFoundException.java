package com.goapi.goapi.exception.securityToken;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Token not found!")
public class TokenNotFoundException extends RuntimeException {

    public TokenNotFoundException(String token) {
        super(String.format("Token with content = '%s' not found!", token));
    }
}
