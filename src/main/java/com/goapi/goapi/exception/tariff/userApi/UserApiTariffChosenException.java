package com.goapi.goapi.exception.tariff.userApi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User api tariff not chosen!")
public class UserApiTariffChosenException extends RuntimeException {

    public UserApiTariffChosenException(Integer userId) {
        super(String.format("User with id = '%s' cant create new api, because user api tariff not chosen!", userId));
    }
}
