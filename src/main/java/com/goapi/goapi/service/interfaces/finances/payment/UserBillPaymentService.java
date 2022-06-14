package com.goapi.goapi.service.interfaces.finances.payment;

import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.finances.payment.UserBillPayment;

import java.math.BigDecimal;

public interface UserBillPaymentService {
    UserBillPayment createUserBillPayment(BigDecimal sum, String description, UserBill userBill);
}
