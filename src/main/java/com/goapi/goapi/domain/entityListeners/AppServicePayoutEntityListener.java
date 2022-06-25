package com.goapi.goapi.domain.entityListeners;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayout;
import com.goapi.goapi.domain.model.finances.payment.paymentStatus.PaymentStatusReason;

import javax.persistence.PrePersist;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Daniil Dmitrochenkov
 **/
public class AppServicePayoutEntityListener  extends PaymentListener {

    @PrePersist
    public void beforeInsert(AppServicePayout appServicePayout) {
        AppServiceBill appServiceBill = appServicePayout.getAppServiceBill();
        BigDecimal appServiceMoney = appServiceBill.getMoneyLeft();
        BigDecimal paymentSum = appServicePayout.getSum();
        if(paymentSum.compareTo(appServiceMoney) >= 0) {
            rejectPayment(appServicePayout, PaymentStatusReason.NOT_ENOUGH_MONEY);
        }
        Date date = appServicePayout.getDate();
        appServiceBill.setLastPayoutDate(date);
        appServiceBill.setMoneyLeft(appServiceMoney.subtract(paymentSum));
        acceptPayment(appServicePayout);
    }

}
