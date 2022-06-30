package com.goapi.goapi.service.implementation.finances.payment;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayout;
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
    public AppServicePayout createAppServicePayout(BigDecimal sum, AppServiceBill appServiceBill) {
        AppServicePayout appServicePayout = new AppServicePayout(sum, payoutDesc, appServiceBill);
        AppServicePayout payout = appServicePayoutRepository.save(appServicePayout);
        return payout;
    }

}
