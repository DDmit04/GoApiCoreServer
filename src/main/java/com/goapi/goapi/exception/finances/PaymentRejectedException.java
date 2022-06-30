package com.goapi.goapi.exception.finances;

import com.goapi.goapi.domain.model.finances.payment.paymentStatus.PaymentStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
public class PaymentRejectedException extends Exception {

    public PaymentRejectedException(Integer paymentId, PaymentStatus paymentStatus) {
        super(String.format("Payment with id = '%s' was rejected (reason = '%s')", paymentId, paymentStatus.getPaymentStatusReason().toString()));
    }
}
