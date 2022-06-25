package com.goapi.goapi.exception.finances.payment;

import com.goapi.goapi.domain.model.finances.payment.Payment;

/**
 * @author Daniil Dmitrochenkov
 **/
public class AppServicePayoutRejectedException extends Exception {

    public AppServicePayoutRejectedException(Payment payment) {
        super(String.format("Payout with data = '%s' was rejected!", payment));
    }
}
