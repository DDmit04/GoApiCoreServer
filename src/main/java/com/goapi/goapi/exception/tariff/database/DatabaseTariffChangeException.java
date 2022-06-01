package com.goapi.goapi.exception.tariff.database;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error while changing database tariff")
public class DatabaseTariffChangeException extends RuntimeException {

    public DatabaseTariffChangeException(Integer dbId, Integer newTariffId, Integer currentTariffId) {
        super(String.format("Fail update database tariff for database with id = '%s' (new tariff id = '%s', current tariff id = ''%s)", newTariffId, currentTariffId, dbId));
    }
}
