package com.goapi.goapi.domain.entityListeners;

import com.goapi.goapi.domain.model.finances.payment.Payment;
import com.goapi.goapi.domain.model.finances.payment.paymentStatus.PaymentStatus;
import com.goapi.goapi.domain.model.finances.payment.paymentStatus.PaymentStatusReason;
import com.goapi.goapi.domain.model.finances.payment.paymentStatus.PaymentStatusType;

/**
 * @author Daniil Dmitrochenkov
 **/
public class PaymentListener {

    public void acceptPayment(Payment payment) {
        setPaymentStatus(payment, PaymentStatusType.ACCEPTED, PaymentStatusReason.EMPTY);
    }

    public void rejectPayment(Payment payment, PaymentStatusReason rejectReason) {
        setPaymentStatus(payment, PaymentStatusType.REJECTED, rejectReason);
    }

    private void setPaymentStatus(Payment payment, PaymentStatusType status, PaymentStatusReason reason) {
        PaymentStatus paymentStatus = payment.getStatus();
        paymentStatus.setPaymentStatusReason(reason);
        paymentStatus.setPaymentStatusType(status);
    }
}
