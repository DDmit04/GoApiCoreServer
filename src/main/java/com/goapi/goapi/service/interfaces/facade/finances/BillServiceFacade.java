package com.goapi.goapi.service.interfaces.facade.finances;

import com.goapi.goapi.domain.dto.payments.appServiceBill.AppServiceBillDto;
import com.goapi.goapi.domain.dto.payments.userBIll.UserBillDto;
import com.goapi.goapi.domain.model.user.User;

public interface BillServiceFacade {
    AppServiceBillDto getUserApiBillDto(User user, Integer apiId);

    AppServiceBillDto getDatabaseBillDto(User user, Integer dbId);

    UserBillDto getUserBillDto(User user);
}
