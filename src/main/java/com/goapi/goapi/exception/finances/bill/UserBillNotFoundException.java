package com.goapi.goapi.exception.finances.bill;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User bill not found")
public class UserBillNotFoundException extends RuntimeException {

    public UserBillNotFoundException(Integer billId) {
        super(String.format("Bill with id = '%s' not found!", billId));
    }
}
