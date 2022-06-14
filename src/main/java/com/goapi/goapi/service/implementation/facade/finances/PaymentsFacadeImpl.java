package com.goapi.goapi.service.implementation.facade.finances;

import com.goapi.goapi.controller.forms.payments.AppServicePaymentData;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.BillType;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.payments.AppServicePaymentProcessException;
import com.goapi.goapi.exception.payments.BillsOwnershipException;
import com.goapi.goapi.exception.payments.UserBillNotEnoughMoneyException;
import com.goapi.goapi.service.interfaces.facade.finances.PaymentsFacade;
import com.goapi.goapi.service.interfaces.finances.bill.BillService;
import com.goapi.goapi.service.interfaces.finances.payment.AppServiceBillPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class PaymentsFacadeImpl implements PaymentsFacade {

    private final AppServiceBillPaymentService appServiceBillPaymentService;
    private final BillService billService;

    @Override
    @Transactional
    public void makeAppServicePayment(User user, AppServicePaymentData appServicePaymentData) {
        Integer userId = user.getId();
        Integer appServiceBillId = appServicePaymentData.getAppServiceBillId();
        AppServiceBill appServiceBill = billService.getAppServiceBillByIdWithOwner(appServiceBillId);
        BigDecimal paymentSum = appServicePaymentData.getSum();
        UserBill userBill = billService.getUserBill(user);
        Integer userBillId = userBill.getId();
        boolean isUserBillsOwner = appServiceBill.getUser().equals(user);
        boolean isEnoughMoney = userBill.getMoneyLeft().compareTo(paymentSum) > -1;
        boolean canMakePayment = isEnoughMoney && isUserBillsOwner;
        if (canMakePayment) {
            String username = user.getUsername();
            BillType appBillType = appServiceBill.getBillType();
            String paymentDesc = String.format("Payment from user '%s' to app service bill with type '%s'", username, appBillType.toString());
            appServiceBillPaymentService.createAppServiceBillPayment(paymentSum, paymentDesc, userBill, appServiceBill);
            return;
        } else if (!isEnoughMoney) {
            throw new UserBillNotEnoughMoneyException(userBillId);
        } else if (!isUserBillsOwner) {
            throw new BillsOwnershipException(userId, appServiceBillId);
        }
        throw new AppServicePaymentProcessException(userId, userBillId, appServicePaymentData);
    }

}
