package com.goapi.goapi.exception.finances.payment;

import com.goapi.goapi.domain.model.finances.payment.Payment;

/**
 * @author Daniil Dmitrochenkov
 **/
public class UserPaymentRejectedException extends Exception {

    public UserPaymentRejectedException(Payment payment) {
        super(String.format("Payment with data = '%s' was rejected!", payment));
    }
}
