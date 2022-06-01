package com.goapi.goapi.service.interfaces.facase;

import com.goapi.goapi.domain.model.user.User;

public interface UserApiTariffServiceFacade {

    boolean changeUserApiTariff(User user, Integer tariffId);

    void setUserApiTariff(User user, Integer tariffId);
}
