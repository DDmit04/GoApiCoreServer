package com.goapi.goapi.exception.finances.bill.userBill;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User bill not found")
public class UserBillNotFoundException extends RuntimeException {

    public UserBillNotFoundException(Integer userId) {
        super(String.format("Bill for user with id = '%s' not found!", userId));
    }
}
