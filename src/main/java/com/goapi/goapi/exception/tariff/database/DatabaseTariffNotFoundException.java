package com.goapi.goapi.exception.tariff.database;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Database tariff not found")
public class DatabaseTariffNotFoundException extends RuntimeException {

    public DatabaseTariffNotFoundException(Integer tariffId) {
        super(String.format("Database tariff with id = '%s' not found!", tariffId));
    }
}
