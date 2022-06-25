package com.goapi.goapi.service.interfaces.finances.payment;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayment;
import com.goapi.goapi.exception.finances.payment.AppServicePaymentRejectedException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
public interface AppServicePaymentService {

    @Transactional(noRollbackFor = AppServicePaymentRejectedException.class)
    AppServicePayment createAppServiceBillPayment(
        BigDecimal sum, String description, UserBill fromUserBill, AppServiceBill appServiceBill) throws AppServicePaymentRejectedException;
}
