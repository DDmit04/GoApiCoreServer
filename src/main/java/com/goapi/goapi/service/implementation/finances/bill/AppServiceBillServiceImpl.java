package com.goapi.goapi.service.implementation.finances.bill;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.BillType;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.finances.bill.appServiceBill.AppServiceBillNotFoundException;
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
    public AppServiceBill createAppServiceBill(User user, BillType billType) {
        AppServiceBill databaseBill = new AppServiceBill(user, billType);
        return databaseBill;
    }
    @Override
    public AppServiceBill getAppServiceBillByIdWithOwnerAndPayments(Integer appServiceBillId) {
        Optional<AppServiceBill> appServiceBill = appServiceBillRepository.findByIdWithOwnerAndPayments(appServiceBillId);
        return appServiceBill.orElseThrow(() -> new AppServiceBillNotFoundException(appServiceBillId));
    }

}
