package com.goapi.goapi.service.interfaces.facade.finances;

import com.goapi.goapi.domain.dto.finances.appServiceBill.AppServiceBillDto;
import com.goapi.goapi.domain.model.user.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AppServiceBillServiceFacade {

    AppServiceBillDto getAppServiceBillDto(User user, Integer appServiceId);
}
