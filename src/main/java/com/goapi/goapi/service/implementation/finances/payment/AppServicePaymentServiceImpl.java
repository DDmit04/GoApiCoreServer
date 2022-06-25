package com.goapi.goapi.service.implementation.finances.payment;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayment;
import com.goapi.goapi.domain.model.finances.payment.paymentStatus.PaymentStatus;
import com.goapi.goapi.domain.model.finances.payment.paymentStatus.PaymentStatusType;
import com.goapi.goapi.exception.finances.payment.AppServicePaymentRejectedException;
import com.goapi.goapi.repo.finances.payment.AppServiceBillPaymentRepository;
import com.goapi.goapi.service.interfaces.finances.payment.AppServicePaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class AppServicePaymentServiceImpl implements AppServicePaymentService {

    private final AppServiceBillPaymentRepository appServiceBillPaymentRepository;

    @Override
    public AppServicePayment createAppServiceBillPayment(
        BigDecimal sum, String description, UserBill fromUserBill, AppServiceBill appServiceBill) throws AppServicePaymentRejectedException {
        AppServicePayment newAppServicePayment = new AppServicePayment(
            sum,
            description,
            fromUserBill,
            appServiceBill
        );
        newAppServicePayment = appServiceBillPaymentRepository.save(newAppServicePayment);
        PaymentStatus paymentStatus = newAppServicePayment.getStatus();
        PaymentStatusType paymentStatusType = paymentStatus.getPaymentStatusType();
        if(paymentStatusType == PaymentStatusType.REJECTED) {
            throw new AppServicePaymentRejectedException(newAppServicePayment);
        }
        return newAppServicePayment;
    }

}
