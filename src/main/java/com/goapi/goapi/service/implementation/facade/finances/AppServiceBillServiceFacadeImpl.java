package com.goapi.goapi.service.implementation.facade.finances;

import com.goapi.goapi.domain.dto.finances.BasePaymentDto;
import com.goapi.goapi.domain.dto.finances.appServiceBill.AppServiceBillDto;
import com.goapi.goapi.domain.model.appService.AppServiceObject;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.BillType;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayment;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayout;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.finances.bill.BillsOwnershipException;
import com.goapi.goapi.service.implementation.finances.payment.PaymentService;
import com.goapi.goapi.service.interfaces.appService.AppServiceObjectService;
import com.goapi.goapi.service.interfaces.facade.finances.AppServiceBillServiceFacade;
import com.goapi.goapi.service.interfaces.finances.bill.AppServiceBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class AppServiceBillServiceFacadeImpl implements AppServiceBillServiceFacade {

    private final AppServiceBillService appServiceBillService;
    private final AppServiceObjectService appServiceObjectService;
    private final PaymentService paymentService;


    @Override
    public AppServiceBillDto getAppServiceBillDto(User user, Integer appServiceId) {
        AppServiceObject appServiceObject = appServiceObjectService.getAppServiceObjectByIdWithOwner(appServiceId);
        Integer appServiceBillId = appServiceObject.getAppServiceBill().getId();
        AppServiceBill appServiceBill = appServiceBillService.getAppServiceBillByIdWithOwnerAndPayments(appServiceBillId);
        boolean isUserBillsOwner = appServiceBill.getUser().equals(user);
        if (!isUserBillsOwner) {
            Integer userId = user.getId();
            throw new BillsOwnershipException(userId, appServiceBillId);
        }
        AppServiceBillDto appServiceBillDto = convertAppServiceBillToDto(appServiceBill);
        return appServiceBillDto;
    }

    private AppServiceBillDto convertAppServiceBillToDto(AppServiceBill appServiceBill) {
        Integer appServiceBillId = appServiceBill.getId();
        Set<AppServicePayment> toAppServicePayments = appServiceBill.getToAppServicePayments();
        Set<AppServicePayout> payouts = appServiceBill.getAppServicePayouts();
        List<BasePaymentDto> paymentDtos = paymentService.convertPaymentsToDtos(toAppServicePayments);
        List<BasePaymentDto> payoutDtos = paymentService.convertPaymentsToDtos(payouts);
        BillType billType = appServiceBill.getBillType();
        BigDecimal appServiceBillMoneyLeft = appServiceBill.getMoneyLeft();
        AppServiceBillDto appServiceBillDto =
            new AppServiceBillDto(
                appServiceBillId,
                appServiceBillMoneyLeft,
                billType,
                paymentDtos,
                payoutDtos);
        return appServiceBillDto;
    }

}
