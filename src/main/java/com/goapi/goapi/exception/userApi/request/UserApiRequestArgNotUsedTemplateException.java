package com.goapi.goapi.exception.userApi.request;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User api request template invalid!")
public class UserApiRequestArgNotUsedTemplateException extends RuntimeException {
    public UserApiRequestArgNotUsedTemplateException(String argName) {
        super(String.format("Argument with name '%s' not found in template", argName));
    }
}
