package com.goapi.goapi.service.interfaces.finances.bill;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.BillType;
import com.goapi.goapi.domain.model.user.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AppServiceBillService {

    AppServiceBill createAppServiceBill(User user, BillType billType);
    AppServiceBill getAppServiceBillByIdWithOwnerAndPayments(Integer appServiceBillId);
}
