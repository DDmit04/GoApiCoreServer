package com.goapi.goapi.service.implementation.facade.finances;

import com.goapi.goapi.domain.dto.payments.BasePaymentDto;
import com.goapi.goapi.domain.dto.payments.appServiceBill.AppServiceBillDto;
import com.goapi.goapi.domain.dto.payments.userBIll.UserBillDto;
import com.goapi.goapi.domain.model.appService.AppServiceObject;
import com.goapi.goapi.domain.model.appService.database.Database;
import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.BillType;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.finances.payment.AppServiceBillPayment;
import com.goapi.goapi.domain.model.finances.payment.Payment;
import com.goapi.goapi.domain.model.finances.payment.UserBillPayment;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.payments.BillsOwnershipException;
import com.goapi.goapi.service.interfaces.appService.database.DatabaseService;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiService;
import com.goapi.goapi.service.interfaces.facade.finances.BillServiceFacade;
import com.goapi.goapi.service.interfaces.finances.bill.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class BillServiceFacadeImpl implements BillServiceFacade {

    private final BillService billService;
    private final DatabaseService databaseService;
    private final UserApiService userApiService;

    @Override
    public AppServiceBillDto getUserApiBillDto(User user, Integer apiId) {
        UserApi userApi = userApiService.getUserApiByIdWithOwner(apiId);
        userApiService.isApiOwnerOrThrow(user, userApi);
        AppServiceBillDto appServiceBillDto = getAppServiceBillDto(user, userApi);
        return appServiceBillDto;
    }

    @Override
    public AppServiceBillDto getDatabaseBillDto(User user, Integer dbId) {
        Database db = databaseService.getDatabaseByIdWithOwner(dbId);
        databaseService.isDatabaseOwnerOrThrow(user, db);
        AppServiceBillDto appServiceBillDto = getAppServiceBillDto(user, db);
        return appServiceBillDto;
    }

    @Override
    public UserBillDto getUserBillDto(User user) {
        UserBill userBill = billService.getUserBill(user);
        Integer userBillId = userBill.getId();
        BigDecimal userBillMoneyLeft = userBill.getMoneyLeft();
        Set<UserBillPayment> userBillPayments = userBill.getUserBillPayments();
        Set<AppServiceBillPayment> appServiceBillPayments = userBill.getAppServiceBillPayments();
        List<BasePaymentDto> userBillPaymentDtos = convertPaymentsToDtos(userBillPayments);
        List<BasePaymentDto> userAppServiceBillPaymentDtos = convertPaymentsToDtos(appServiceBillPayments);
        UserBillDto userBillDto =
            new UserBillDto(
                userBillId,
                userBillMoneyLeft,
                userBillPaymentDtos,
                userAppServiceBillPaymentDtos
            );
        return userBillDto;
    }

    private AppServiceBillDto getAppServiceBillDto(User user, AppServiceObject appServiceObject) {
        Integer appServiceBillId = appServiceObject.getAppServiceBill().getId();
        AppServiceBill appServiceBill = billService.getAppServiceBillByIdWithOwner(appServiceBillId);
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
        Set<AppServiceBillPayment> toAppServicePayments = appServiceBill.getToAppServicePayments();
        List<BasePaymentDto> paymentDtos = convertPaymentsToDtos(toAppServicePayments);
        BillType billType = appServiceBill.getBillType();
        BigDecimal appServiceBillMoneyLeft = appServiceBill.getMoneyLeft();
        AppServiceBillDto appServiceBillDto =
            new AppServiceBillDto(
                appServiceBillId,
                appServiceBillMoneyLeft,
                billType,
                paymentDtos
            );
        return appServiceBillDto;
    }

    private List<BasePaymentDto> convertPaymentsToDtos(Set<? extends Payment> toAppServicePayments) {
        List<BasePaymentDto> paymentDtos = toAppServicePayments
            .stream()
            .map(payment -> {
                Integer paymentId = payment.getId();
                LocalDateTime paymentDate = payment.getDate();
                BigDecimal paymentSum = payment.getSum();
                String paymentDescription = payment.getDescription();
                BasePaymentDto appServiceBillPaymentDto =
                    new BasePaymentDto(
                        paymentId,
                        paymentDate,
                        paymentSum,
                        paymentDescription
                    );
                return appServiceBillPaymentDto;
            }).collect(Collectors.toList());
        return paymentDtos;
    }
}
