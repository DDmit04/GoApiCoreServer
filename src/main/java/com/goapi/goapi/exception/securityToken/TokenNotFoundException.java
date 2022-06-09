package com.goapi.goapi.exception.securityToken;

import com.goapi.goapi.domain.model.token.TokenType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Token not found!")
public class TokenNotFoundException extends RuntimeException {

    public TokenNotFoundException(String token, TokenType tokenType) {
        super(String.format("Token with type = '%s' and content = '%s' not found!", tokenType, token));
    }
}
