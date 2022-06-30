package com.goapi.goapi.service.interfaces.finances.bill;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AppServiceBillService {

    AppServiceBill getAppServiceBillByIdAndUserIdWithAllPayments(Integer userId, Integer appServiceId);

    AppServiceBill createUserApiBill();
    AppServiceBill createDatabaseBill();
}
