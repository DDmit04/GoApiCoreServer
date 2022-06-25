package com.goapi.goapi.exception.finances.payment;

import com.goapi.goapi.domain.model.appService.AppServiceObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;


/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error while processing payment")
public class PaymentProcessingException extends RuntimeException {

    public PaymentProcessingException(Integer userId, AppServiceObject appServiceObject, BigDecimal paymentSum) {
        super(String.format("Payment from user with id = '%s' on sum = '%s' processing error! app service object data = '%s'", userId, paymentSum, appServiceObject));
    }
}
