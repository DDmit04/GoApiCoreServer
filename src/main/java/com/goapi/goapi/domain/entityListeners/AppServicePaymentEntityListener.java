package com.goapi.goapi.domain.entityListeners;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayment;
import com.goapi.goapi.domain.model.finances.payment.paymentStatus.PaymentStatusReason;

import javax.persistence.PrePersist;
import java.math.BigDecimal;

/**
 * @author Daniil Dmitrochenkov
 **/
public class AppServicePaymentEntityListener extends PaymentListener {

    @PrePersist
    public void beforeInsert(AppServicePayment appServicePayment) {
        UserBill fromUserBill = appServicePayment.getFromUserBill();
        AppServiceBill toAppServiceBill = appServicePayment.getToAppServiceBill();
        BigDecimal userBillMoney = fromUserBill.getMoneyLeft();
        BigDecimal paymentSum = appServicePayment.getSum();
        if(paymentSum.compareTo(userBillMoney) >= 0) {
            rejectPayment(appServicePayment, PaymentStatusReason.NOT_ENOUGH_MONEY);
        }
        BigDecimal appServiceMoney = toAppServiceBill.getMoneyLeft();
        fromUserBill.setMoneyLeft(userBillMoney.subtract(paymentSum));
        toAppServiceBill.setMoneyLeft(appServiceMoney.add(paymentSum));
        acceptPayment(appServicePayment);
    }

}
