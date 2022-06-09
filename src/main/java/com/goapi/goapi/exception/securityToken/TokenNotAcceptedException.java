package com.goapi.goapi.exception.securityToken;

import com.goapi.goapi.domain.model.token.TokenType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Token not accepted!")
public class TokenNotAcceptedException extends RuntimeException {

    public TokenNotAcceptedException(Integer tokenId, TokenType tokenType, TokenType targetTokenType, boolean isMatch) {
        super(String.format("Token with id = '%s' and type = '%s' (target type = '%') passed validation = '%s' was not accepted", tokenId, tokenType, targetTokenType, isMatch));
    }
}
