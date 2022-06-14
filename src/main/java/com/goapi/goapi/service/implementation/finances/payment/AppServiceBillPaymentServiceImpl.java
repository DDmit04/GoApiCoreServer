package com.goapi.goapi.service.implementation.finances.payment;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.finances.payment.AppServiceBillPayment;
import com.goapi.goapi.repo.finances.payment.AppServiceBillPaymentRepository;
import com.goapi.goapi.service.interfaces.finances.payment.AppServiceBillPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class AppServiceBillPaymentServiceImpl implements AppServiceBillPaymentService {

    private final AppServiceBillPaymentRepository appServiceBillPaymentRepository;

    @Override
    public AppServiceBillPayment createAppServiceBillPayment(
        BigDecimal sum, String description, UserBill fromUserBill, AppServiceBill appServiceBill) {
        AppServiceBillPayment newAppServiceBillPayment = new AppServiceBillPayment(
            sum,
            description,
            fromUserBill,
            appServiceBill
        );
        newAppServiceBillPayment = appServiceBillPaymentRepository.save(newAppServiceBillPayment);
        return newAppServiceBillPayment;
    }

}
