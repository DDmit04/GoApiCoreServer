package com.goapi.goapi.service.interfaces.finances.payment;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayout;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
public interface AppServicePayoutService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    AppServicePayout createAppServicePayout(BigDecimal sum, AppServiceBill appServiceBill);
}
