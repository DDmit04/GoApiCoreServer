package com.goapi.goapi.service.interfaces.finances.payment;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.finances.payment.AppServiceBillPayment;

import java.math.BigDecimal;

public interface AppServiceBillPaymentService {
    AppServiceBillPayment createAppServiceBillPayment(
        BigDecimal sum, String description, UserBill fromUserBill, AppServiceBill appServiceBill);
}
