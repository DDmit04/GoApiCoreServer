package com.goapi.goapi.service.implementation.finances.payment;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayout;
import com.goapi.goapi.domain.model.finances.payment.paymentStatus.PaymentStatus;
import com.goapi.goapi.domain.model.finances.payment.paymentStatus.PaymentStatusType;
import com.goapi.goapi.exception.finances.payment.AppServicePayoutRejectedException;
import com.goapi.goapi.repo.finances.payment.AppServicePayoutRepository;
import com.goapi.goapi.service.interfaces.finances.payment.AppServicePayoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class AppServicePayoutServiceImpl implements AppServicePayoutService {

    private final AppServicePayoutRepository appServicePayoutRepository;
    @Value("${string.payout-description}")
    private String payoutDesc;
    @Override
    public AppServicePayout createPayout(BigDecimal sum, AppServiceBill appServiceBill) throws AppServicePayoutRejectedException {
        AppServicePayout appServicePayout = new AppServicePayout(sum, payoutDesc, appServiceBill);
        AppServicePayout payout = appServicePayoutRepository.save(appServicePayout);
        PaymentStatus status = payout.getStatus();
        if(status.getPaymentStatusType() == PaymentStatusType.REJECTED) {
            throw new AppServicePayoutRejectedException(payout);
        }
        return payout;
    }

}
