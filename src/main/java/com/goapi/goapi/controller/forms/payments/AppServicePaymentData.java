package com.goapi.goapi.controller.forms.payments;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class AppServicePaymentData {

    @PositiveOrZero(message = "app service id must be positive!")
    @NotNull(message = "app service id can't be null!")
    private final Integer appServiceId;
    @PositiveOrZero(message = "payment sum must be positive!")
    @NotNull(message = "payment sum can't be null!")
    private final BigDecimal sum;

    public AppServicePaymentData(Integer appServiceId, BigDecimal sum) {
        this.appServiceId = appServiceId;
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "AppServicePaymentData{" +
            "appServiceBillId=" + appServiceId +
            ", sum=" + sum +
            '}';
    }
}
