package com.goapi.goapi.service.interfaces.finances.bill;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.user.User;

public interface BillService {
    UserBill getUserBill(User user);

    AppServiceBill getAppServiceBillByIdWithOwner(Integer appServiceBillId);
}
