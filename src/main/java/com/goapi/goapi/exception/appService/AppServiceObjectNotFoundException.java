package com.goapi.goapi.exception.appService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "App service object not found")
public class AppServiceObjectNotFoundException extends RuntimeException {

    public AppServiceObjectNotFoundException(Integer appServiceObjectId) {
        super(String.format("App service object with id = '%s' not found!", appServiceObjectId));
    }
}
