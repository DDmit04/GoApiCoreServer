package com.goapi.goapi.exception.finances.payment;

import com.goapi.goapi.domain.model.finances.payment.Payment;

/**
 * @author Daniil Dmitrochenkov
 **/
public class AppServicePaymentRejectedException extends Exception {

    public AppServicePaymentRejectedException(Payment payment) {
        super(String.format("Payment with data = '%s' was rejected!", payment));
    }
}
