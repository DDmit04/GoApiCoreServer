package com.goapi.goapi.exception.tariff.userApi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User api tariff not found")
public class UserApiTariffNotFoundException extends RuntimeException {

    public UserApiTariffNotFoundException(Integer apiId) {
        super(String.format("User api tariff with id = '%s' not found!", apiId));
    }
}
