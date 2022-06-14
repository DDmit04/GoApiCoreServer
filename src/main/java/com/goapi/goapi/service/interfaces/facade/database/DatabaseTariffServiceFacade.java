package com.goapi.goapi.service.interfaces.facade.database;

import com.goapi.goapi.domain.dto.tariff.DatabaseTariffDto;
import com.goapi.goapi.domain.model.user.User;

public interface DatabaseTariffServiceFacade {
    void changeDatabaseTariff(User user, Integer dbId, Integer tariffId);
    DatabaseTariffDto getDatabaseTariff(User user, Integer dbId);
}
