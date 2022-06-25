package com.goapi.goapi.service.interfaces.finances.payment;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayout;
import com.goapi.goapi.exception.finances.payment.AppServicePaymentRejectedException;
import com.goapi.goapi.exception.finances.payment.AppServicePayoutRejectedException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
public interface AppServicePayoutService {
    @Transactional(noRollbackFor = AppServicePaymentRejectedException.class)
    AppServicePayout createPayout(BigDecimal sum, AppServiceBill appServiceBill) throws AppServicePayoutRejectedException;
}
