package com.goapi.goapi.exception.tariff.database;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Database tariff can't be changed")
public class DatabaseTariffConditionChangeException extends RuntimeException {

    public DatabaseTariffConditionChangeException(Integer dbId, Integer newTariffId, Integer currentTariffId) {
        super(String.format("New database tariff with id = '%s' (current tariff id = '%s') for database with id = '%s' can't set!", newTariffId, currentTariffId, dbId));
    }
}
