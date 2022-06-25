package com.goapi.goapi.domain.model.finances.payment.paymentStatus;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Embeddable
@Data
public class PaymentStatus {

    @NotNull(message = "payment status can't be null!")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "payment_status")
    private PaymentStatusType paymentStatusType;

    @NotNull(message = "payment reason can't be null!")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "payment_status_reason")
    private PaymentStatusReason paymentStatusReason;

    public PaymentStatus() {
        this.paymentStatusType = PaymentStatusType.PROCESSING;
        this.paymentStatusReason = PaymentStatusReason.EMPTY;
    }
}