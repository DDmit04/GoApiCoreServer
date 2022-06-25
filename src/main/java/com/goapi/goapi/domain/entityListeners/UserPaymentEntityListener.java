package com.goapi.goapi.domain.entityListeners;

import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.finances.payment.UserPayment;

import javax.persistence.PrePersist;
import java.math.BigDecimal;

/**
 * @author Daniil Dmitrochenkov
 **/
public class UserPaymentEntityListener  extends PaymentListener {

    @PrePersist
    public void beforeInsert(UserPayment userPayment) {
        UserBill userBill = userPayment.getUserBill();
        BigDecimal userBillMoney = userBill.getMoneyLeft();
        BigDecimal sum = userPayment.getSum();
        userBill.setMoneyLeft(userBillMoney.add(sum));
        acceptPayment(userPayment);
    }

}
