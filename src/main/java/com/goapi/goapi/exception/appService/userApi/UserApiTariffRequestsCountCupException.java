package com.goapi.goapi.exception.appService.userApi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "User api requests count is max for current user api tariff")
public class UserApiTariffRequestsCountCupException extends RuntimeException {

    public UserApiTariffRequestsCountCupException(Integer userApiId) {
        super(String.format("Can't add new api request to api with id = '%s', because reach max requests count for current user api tariff!", userApiId));
    }
}
