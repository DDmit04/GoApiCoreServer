package com.goapi.goapi.service.implementation.finances.payment;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayment;
import com.goapi.goapi.repo.finances.payment.AppServicePaymentRepository;
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

    private final AppServicePaymentRepository appServicePaymentRepository;

    @Override
    public AppServicePayment createAppServicePayment(
        BigDecimal sum, String description, UserBill fromUserBill, AppServiceBill appServiceBill) {
        AppServicePayment newAppServicePayment = new AppServicePayment(
            sum,
            description,
            fromUserBill,
            appServiceBill
        );
        newAppServicePayment = appServicePaymentRepository.save(newAppServicePayment);
        return newAppServicePayment;
    }

}
