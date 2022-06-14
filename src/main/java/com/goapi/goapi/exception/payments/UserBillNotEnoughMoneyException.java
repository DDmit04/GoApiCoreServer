package com.goapi.goapi.exception.payments;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "Not enough money on user bill")
public class UserBillNotEnoughMoneyException extends RuntimeException {

    public UserBillNotEnoughMoneyException(Integer billId) {
        super(String.format("User bill with id = '%s' has not enough money for make payment!", billId));
    }
}
