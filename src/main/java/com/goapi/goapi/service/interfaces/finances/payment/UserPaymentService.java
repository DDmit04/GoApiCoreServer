package com.goapi.goapi.service.interfaces.finances.payment;

import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.finances.payment.UserPayment;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
public interface UserPaymentService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    UserPayment createUserBillPayment(BigDecimal sum, String description, UserBill userBill);
}
