package com.goapi.goapi.controller.forms.payments;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class AppServicePaymentData {

    private final Integer appServiceBillId;
    private final BigDecimal sum;

    public AppServicePaymentData(Integer appServiceBillId, BigDecimal sum) {
        this.appServiceBillId = appServiceBillId;
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "AppServicePaymentData{" +
            "appServiceBillId=" + appServiceBillId +
            ", sum=" + sum +
            '}';
    }
}
