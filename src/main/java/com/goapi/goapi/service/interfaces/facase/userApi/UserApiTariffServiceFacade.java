package com.goapi.goapi.service.interfaces.facase.userApi;

import com.goapi.goapi.domain.dto.tariff.UserApiTariffDto;
import com.goapi.goapi.domain.model.user.User;

public interface UserApiTariffServiceFacade {

    UserApiTariffDto getUserApiTariff(User user);

    boolean changeUserApiTariff(User user, Integer tariffId);

    void setUserApiTariff(User user, Integer tariffId);
}
