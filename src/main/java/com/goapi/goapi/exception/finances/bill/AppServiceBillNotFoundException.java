package com.goapi.goapi.exception.finances.bill;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "App service bill not found")
public class AppServiceBillNotFoundException extends RuntimeException {

    public AppServiceBillNotFoundException(Integer billId) {
        super(String.format("App service bill with id = '%s' not found!", billId));
    }

}
