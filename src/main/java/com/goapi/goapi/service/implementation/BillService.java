package com.goapi.goapi.service.implementation;

import com.goapi.goapi.domain.model.bill.AppServiceBill;
import com.goapi.goapi.domain.model.bill.BillType;
import com.goapi.goapi.domain.model.bill.UserBill;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.repo.finances.bill.AppServiceBillRepository;
import com.goapi.goapi.repo.finances.bill.UserBillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class BillService {

    private final UserBillRepository userBillRepository;
    private final AppServiceBillRepository appServiceBillRepository;

    public UserBill createUserBill() {
        UserBill userBill = new UserBill();
        userBill = userBillRepository.save(userBill);
        return userBill;
    }
    public AppServiceBill createDatabaseBill(User owner) {
        AppServiceBill appServiceBill = createBillAppServiceBill(owner, BillType.DATABASE);
        return appServiceBill;
    }
    public AppServiceBill createUserApiBill(User owner) {
        AppServiceBill appServiceBill = createBillAppServiceBill(owner, BillType.USER_API);
        return appServiceBill;
    }

    private AppServiceBill createBillAppServiceBill(User owner, BillType billType) {
        AppServiceBill appServiceBill = new AppServiceBill(owner, billType);
        appServiceBill = appServiceBillRepository.save(appServiceBill);
        return appServiceBill;
    }


}
