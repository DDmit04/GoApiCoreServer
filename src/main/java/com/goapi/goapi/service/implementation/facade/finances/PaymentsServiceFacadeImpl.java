package com.goapi.goapi.service.implementation.facade.finances;

import com.goapi.goapi.controller.forms.payments.AppServicePaymentData;
import com.goapi.goapi.domain.model.appService.AppServiceObject;
import com.goapi.goapi.domain.model.appService.AppServiceObjectStatus;
import com.goapi.goapi.domain.model.appService.AppServiceStatusType;
import com.goapi.goapi.domain.model.appService.tariff.Tariff;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.BillType;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayment;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.finances.PaymentRejectedException;
import com.goapi.goapi.service.interfaces.appService.AppServiceObjectService;
import com.goapi.goapi.service.interfaces.appService.appServiceTasks.AppServiceTaskServiceFacade;
import com.goapi.goapi.service.interfaces.facade.finances.PaymentsServiceFacade;
import com.goapi.goapi.service.interfaces.finances.bill.UserBillService;
import com.goapi.goapi.service.interfaces.finances.payment.AppServicePaymentService;
import com.goapi.goapi.service.interfaces.finances.payment.PaymentProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class PaymentsServiceFacadeImpl implements PaymentsServiceFacade {

    private final AppServicePaymentService appServicePaymentService;
    private final AppServiceObjectService appServiceObjectService;
    private final UserBillService userBillService;
    private final AppServiceTaskServiceFacade appServiceTaskServiceFacade;

    private final PaymentProcessingService paymentProcessingService;
    @Value("${string.payment-description-template}")
    private String paymentDescriptionTemplate;

    @Override
    public void makeAppServicePayment(User user, AppServicePaymentData appServicePaymentData) {
        Integer appServiceId = appServicePaymentData.getAppServiceId();
        AppServiceObject appServiceObject = appServiceObjectService.getAppServiceObjectByIdWithTariffAndBill(appServiceId);
        BigDecimal paymentSum = appServicePaymentData.getSum();
        makePayment(user, appServiceObject, paymentSum);
        boolean appServiceDisabled = isAppServiceDisabled(appServiceObject);
        if(appServiceDisabled) {
            tryContinueDisabledAppServicePayouts(appServiceObject, paymentSum);
        }
    }

    @Override
    public void makeFirstAppServicePayment(User user, AppServiceObject appServiceObject) {
        Tariff tariff = appServiceObject.getAppServiceTariff();
        BigDecimal firstPaymentSum = tariff.getCostPerDay();
        makePayment(user, appServiceObject, firstPaymentSum);
        tryContinueDisabledAppServicePayouts(appServiceObject, firstPaymentSum);
    }

    @Override
    public void makeFirstAppServicePaymentAfterTariffChange(User user, AppServiceObject appServiceObject) {
        makeFirstAppServicePayment(user, appServiceObject);
        Tariff tariff = appServiceObject.getAppServiceTariff();
        BigDecimal firstPaymentSum = tariff.getCostPerDay();
        boolean appServiceDisabled = isAppServiceDisabled(appServiceObject);
        if(appServiceDisabled) {
            tryContinueDisabledAppServicePayouts(appServiceObject, firstPaymentSum);
        } else {
            appServiceTaskServiceFacade.restartAppServiceNextPayoutTask(appServiceObject);
        }
    }

    private void makePayment(User user, AppServiceObject appServiceObject, BigDecimal paymentSum) {
        AppServiceBill appServiceBill = appServiceObject.getAppServiceBill();
        UserBill userBill = userBillService.getUserBill(user);
        String username = user.getUsername();
        BillType appBillType = appServiceBill.getBillType();
        String paymentDesc = String.format(paymentDescriptionTemplate, username, appBillType.toString());
        try {
            AppServicePayment appServicePayment = appServicePaymentService.createAppServicePayment(paymentSum, paymentDesc, userBill, appServiceBill);
            paymentProcessingService.processAppServicePayment(appServicePayment);
        } catch (PaymentRejectedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    private void tryContinueDisabledAppServicePayouts(AppServiceObject appServiceObject, BigDecimal paymentSum) {
        Tariff appServiceTariff = appServiceObject.getAppServiceTariff();
        BigDecimal costPerDay = appServiceTariff.getCostPerDay();
        boolean canEnableService = costPerDay.compareTo(paymentSum) <= 0;
        if (canEnableService) {
            appServiceObjectService.enableAppServiceObject(appServiceObject);
            appServiceTaskServiceFacade.startAppServiceNextPayoutTask(appServiceObject);
        }
    }

    private boolean isAppServiceDisabled(AppServiceObject appServiceObject) {
        AppServiceObjectStatus appServiceObjectStatus = appServiceObject.getAppServiceObjectStatus();
        AppServiceStatusType appServiceObjectStatusType = appServiceObjectStatus.getStatus();
        return appServiceObjectStatusType == AppServiceStatusType.DISABLED;
    }

}
