package com.goapi.goapi.exception.appService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "User app service object access not allowed")
public class AppServiceObjectOwnerException extends RuntimeException {

    public AppServiceObjectOwnerException(Integer userId, Integer appServiceObjectId) {
        super(String.format("User with id = '%s' isn't owner of app service object with id = '%s'!", userId, appServiceObjectId));
    }
}
