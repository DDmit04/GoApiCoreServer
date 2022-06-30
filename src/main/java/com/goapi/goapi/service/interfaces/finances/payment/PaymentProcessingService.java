package com.goapi.goapi.service.interfaces.finances.payment;

import com.goapi.goapi.domain.model.finances.payment.AppServicePayment;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayout;
import com.goapi.goapi.domain.model.finances.payment.UserPayment;
import com.goapi.goapi.exception.finances.PaymentRejectedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(noRollbackFor = PaymentRejectedException.class)
public interface PaymentProcessingService {

    @Transactional(propagation = Propagation.MANDATORY, noRollbackFor = PaymentRejectedException.class)
    void processAppServicePayout(AppServicePayout appServicePayout) throws PaymentRejectedException;

    @Transactional(propagation = Propagation.MANDATORY, noRollbackFor = PaymentRejectedException.class)
    void processAppServicePayment(AppServicePayment appServicePayment) throws PaymentRejectedException;

    @Transactional(propagation = Propagation.MANDATORY, noRollbackFor = PaymentRejectedException.class)
    void processUserPayment(UserPayment userPayment) throws PaymentRejectedException;
}
