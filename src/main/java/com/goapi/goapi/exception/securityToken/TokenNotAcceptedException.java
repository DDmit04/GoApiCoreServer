package com.goapi.goapi.exception.securityToken;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Token not accepted!")
public class TokenNotAcceptedException extends RuntimeException {

    public TokenNotAcceptedException(Integer tokenId) {
        super(String.format("Token with id = '%s' was not accepted", tokenId));
    }
}
