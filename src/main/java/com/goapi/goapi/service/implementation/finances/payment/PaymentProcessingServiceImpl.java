package com.goapi.goapi.service.implementation.finances.payment;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayment;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayout;
import com.goapi.goapi.domain.model.finances.payment.Payment;
import com.goapi.goapi.domain.model.finances.payment.UserPayment;
import com.goapi.goapi.domain.model.finances.payment.paymentStatus.PaymentStatus;
import com.goapi.goapi.domain.model.finances.payment.paymentStatus.PaymentStatusReason;
import com.goapi.goapi.domain.model.finances.payment.paymentStatus.PaymentStatusType;
import com.goapi.goapi.exception.finances.PaymentRejectedException;
import com.goapi.goapi.repo.finances.bill.AppServiceBillRepository;
import com.goapi.goapi.repo.finances.bill.UserBillRepository;
import com.goapi.goapi.repo.finances.payment.AppServicePaymentRepository;
import com.goapi.goapi.repo.finances.payment.AppServicePayoutRepository;
import com.goapi.goapi.repo.finances.payment.UserPaymentRepository;
import com.goapi.goapi.service.interfaces.finances.payment.PaymentProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class PaymentProcessingServiceImpl implements PaymentProcessingService {

    private final UserBillRepository userBillRepository;
    private final AppServiceBillRepository appServiceBillRepository;
    private final AppServicePaymentRepository appServicePaymentRepository;
    private final AppServicePayoutRepository appServicePayoutRepository;
    private final UserPaymentRepository userPaymentRepository;

    @Override
    public void processAppServicePayout(AppServicePayout appServicePayout) throws PaymentRejectedException {
        AppServiceBill appServiceBill = appServicePayout.getAppServiceBill();
        BigDecimal appServiceMoney = appServiceBill.getMoneyLeft();
        BigDecimal paymentSum = appServicePayout.getSum();
        if (paymentSum.compareTo(appServiceMoney) > 0) {
            rejectPayment(appServicePayout, PaymentStatusReason.NOT_ENOUGH_MONEY);
        } else {
            acceptPayment(appServicePayout);
            Date date = appServicePayout.getDate();
            appServiceBill.setLastPayoutDate(date);
            appServiceBill.setMoneyLeft(appServiceMoney.subtract(paymentSum));
            appServiceBillRepository.save(appServiceBill);
        }
        appServicePayoutRepository.save(appServicePayout);
        throwIfRejected(appServicePayout);
    }

    @Override
    public void processAppServicePayment(AppServicePayment appServicePayment) throws PaymentRejectedException {
        UserBill fromUserBill = appServicePayment.getFromUserBill();
        AppServiceBill toAppServiceBill = appServicePayment.getToAppServiceBill();
        BigDecimal userBillMoney = fromUserBill.getMoneyLeft();
        BigDecimal paymentSum = appServicePayment.getSum();
        if (paymentSum.compareTo(userBillMoney) > 0) {
            rejectPayment(appServicePayment, PaymentStatusReason.NOT_ENOUGH_MONEY);
        } else {
            BigDecimal appServiceMoney = toAppServiceBill.getMoneyLeft();
            fromUserBill.setMoneyLeft(userBillMoney.subtract(paymentSum));
            toAppServiceBill.setMoneyLeft(appServiceMoney.add(paymentSum));
            acceptPayment(appServicePayment);
            userBillRepository.save(fromUserBill);
            appServiceBillRepository.save(toAppServiceBill);
        }
        appServicePaymentRepository.save(appServicePayment);
        throwIfRejected(appServicePayment);
    }

    @Override
    //TODO keep
    public void processUserPayment(UserPayment userPayment) throws PaymentRejectedException {
        UserBill userBill = userPayment.getUserBill();
        BigDecimal userBillMoney = userBill.getMoneyLeft();
        BigDecimal sum = userPayment.getSum();
        userBill.setMoneyLeft(userBillMoney.add(sum));
        acceptPayment(userPayment);
        userBillRepository.save(userBill);
        userPaymentRepository.save(userPayment);
        throwIfRejected(userPayment);
    }

    private void throwIfRejected(Payment payment) throws PaymentRejectedException {
        PaymentStatus paymentStatus = payment.getStatus();
        PaymentStatusType paymentStatusType = paymentStatus.getPaymentStatusType();
        if (paymentStatusType == PaymentStatusType.REJECTED) {
            Integer paymentId = payment.getId();
            throw new PaymentRejectedException(paymentId, paymentStatus);
        }
    }

    private void acceptPayment(Payment payment) {
        setPaymentStatus(payment, PaymentStatusType.ACCEPTED, PaymentStatusReason.EMPTY);
    }

    private void rejectPayment(Payment payment, PaymentStatusReason rejectReason) {
        setPaymentStatus(payment, PaymentStatusType.REJECTED, rejectReason);
    }

    private void setPaymentStatus(Payment payment, PaymentStatusType status, PaymentStatusReason reason) {
        PaymentStatus paymentStatus = payment.getStatus();
        paymentStatus.setPaymentStatusReason(reason);
        paymentStatus.setPaymentStatusType(status);
    }

}
