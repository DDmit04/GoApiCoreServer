package com.goapi.goapi.service.interfaces.facade.finances;

import com.goapi.goapi.controller.forms.payments.AppServicePaymentData;
import com.goapi.goapi.domain.model.appService.AppServiceObject;
import com.goapi.goapi.domain.model.user.User;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface PaymentsServiceFacade {

    void makeAppServicePayment(User user, AppServicePaymentData appServicePaymentData);
    void makeFirstAppServicePayment(User user, AppServiceObject appServiceObject);
    void makeFirstAppServicePaymentAfterTariffChange(User user, AppServiceObject appServiceObject);
}
