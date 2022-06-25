package com.goapi.goapi.service.interfaces.facade.userApi;

import com.goapi.goapi.domain.dto.appServiceobject.tariff.UserApiTariffDto;
import com.goapi.goapi.domain.model.user.User;

public interface UserApiTariffServiceFacade {

    UserApiTariffDto getUserApiTariff(User user, Integer apiId);

    void changeUserApiTariff(User user, Integer userApiTariffId, Integer tariffId);

}
