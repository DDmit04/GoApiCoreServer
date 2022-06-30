package com.goapi.goapi.service.interfaces.finances.payment;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayment;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
public interface AppServicePaymentService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    AppServicePayment createAppServicePayment(
        BigDecimal sum, String description, UserBill fromUserBill, AppServiceBill appServiceBill);
}
