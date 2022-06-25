package com.goapi.goapi.service.implementation.facade.finances;

import com.goapi.goapi.domain.dto.finances.BasePaymentDto;
import com.goapi.goapi.domain.dto.finances.userBIll.UserBillDto;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayment;
import com.goapi.goapi.domain.model.finances.payment.UserPayment;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.service.implementation.finances.payment.PaymentService;
import com.goapi.goapi.service.interfaces.facade.finances.UserBillServiceFacade;
import com.goapi.goapi.service.interfaces.finances.bill.UserBillService;
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
public class UserBillServiceFacadeImpl implements UserBillServiceFacade {

    private final UserBillService userBillService;
    private final PaymentService paymentService;

    @Override
    public UserBillDto getUserBillDto(User user) {
        UserBill userBill = userBillService.getUserBillWithPayments(user);
        Integer userBillId = userBill.getId();
        BigDecimal userBillMoneyLeft = userBill.getMoneyLeft();
        Set<UserPayment> userPayments = userBill.getUserPayments();
        Set<AppServicePayment> appServicePayments = userBill.getAppServicePayments();
        List<BasePaymentDto> userBillPaymentDtos = paymentService.convertPaymentsToDtos(userPayments);
        List<BasePaymentDto> userAppServiceBillPaymentDtos = paymentService.convertPaymentsToDtos(appServicePayments);
        UserBillDto userBillDto =
            new UserBillDto(
                userBillId,
                userBillMoneyLeft,
                userBillPaymentDtos,
                userAppServiceBillPaymentDtos
            );
        return userBillDto;
    }

}
