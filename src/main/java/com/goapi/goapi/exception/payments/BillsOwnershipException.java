package com.goapi.goapi.exception.payments;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "User can't make payment to this app service bill")
public class BillsOwnershipException extends RuntimeException {

    public BillsOwnershipException(Integer userId, Integer appServiceBillId) {
        super(String.format("User with id = '%s' can't make any action with app service bill with id = '%s'!", userId, appServiceBillId));
    }
}
