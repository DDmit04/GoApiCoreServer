package com.goapi.goapi.service.interfaces.facade.finances;

import com.goapi.goapi.controller.forms.payments.AppServicePaymentData;
import com.goapi.goapi.domain.model.user.User;

import javax.transaction.Transactional;

public interface PaymentsFacade {
    @Transactional
    void makeAppServicePayment(User user, AppServicePaymentData appServicePaymentData);
}
