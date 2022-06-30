package com.goapi.goapi.exception.tariff.userApi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "User api tariff can't be changed")
public class UserApiTariffConditionChangeException extends RuntimeException {

    public UserApiTariffConditionChangeException(Integer newTariffId, Integer currentTariffId) {
        super(String.format("New user api tariff with id = '%s' (current tariff id = ''%s) can't set!", newTariffId, currentTariffId));
    }
}
