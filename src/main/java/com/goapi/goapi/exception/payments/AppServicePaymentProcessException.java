package com.goapi.goapi.exception.payments;

import com.goapi.goapi.controller.forms.payments.AppServicePaymentData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Server cant process this payment")
public class AppServicePaymentProcessException extends RuntimeException {

    public AppServicePaymentProcessException(Integer userId, Integer userBillId, AppServicePaymentData appServicePaymentData) {
        super(String.format("User with id = '%s' and bill id = '%s' can't process payment = '%s'!", userId, userBillId, appServicePaymentData));
    }
}
