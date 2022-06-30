package com.goapi.goapi.service.implementation.finances.bill;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.BillType;
import com.goapi.goapi.exception.finances.bill.AppServiceBillNotFoundException;
import com.goapi.goapi.repo.finances.bill.AppServiceBillRepository;
import com.goapi.goapi.service.interfaces.finances.bill.AppServiceBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class AppServiceBillServiceImpl implements AppServiceBillService {

    private final AppServiceBillRepository appServiceBillRepository;

    @Override
    public AppServiceBill getAppServiceBillByIdAndUserIdWithAllPayments(Integer userId, Integer appServiceId) {
        Optional<AppServiceBill> appServiceBillOptional = appServiceBillRepository.findByIdAndUserIdWithAllPayments(userId, appServiceId);
        return appServiceBillOptional.orElseThrow(() -> new AppServiceBillNotFoundException(appServiceId));
    }

    @Override
    public AppServiceBill createUserApiBill() {
        return createAppServiceBill(BillType.USER_API);
    }

    @Override
    public AppServiceBill createDatabaseBill() {
        return createAppServiceBill(BillType.DATABASE);
    }

    private AppServiceBill createAppServiceBill(BillType billType) {
        AppServiceBill databaseBill = new AppServiceBill(billType);
        return databaseBill;
    }

}
