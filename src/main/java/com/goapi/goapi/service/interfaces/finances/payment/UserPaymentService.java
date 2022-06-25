package com.goapi.goapi.service.interfaces.finances.payment;

import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.finances.payment.UserPayment;
import com.goapi.goapi.exception.finances.payment.UserPaymentRejectedException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
public interface UserPaymentService {

    @Transactional(noRollbackFor = UserPaymentRejectedException.class)
    UserPayment createUserBillPayment(BigDecimal sum, String description, UserBill userBill) throws UserPaymentRejectedException;
}
