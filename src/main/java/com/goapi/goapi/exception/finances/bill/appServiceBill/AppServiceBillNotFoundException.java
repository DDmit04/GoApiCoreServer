package com.goapi.goapi.exception.finances.bill.appServiceBill;

import com.goapi.goapi.domain.model.finances.bill.BillType;
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

    public AppServiceBillNotFoundException(Integer userId, BillType billType) {
        super(String.format("App service bill with type = '%s' for user with id = '%s' not found!", userId, billType));
    }
}
