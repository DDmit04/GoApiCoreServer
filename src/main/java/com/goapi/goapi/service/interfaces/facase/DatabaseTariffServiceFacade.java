package com.goapi.goapi.service.interfaces.facase;

import com.goapi.goapi.domain.dto.tariff.DatabaseTariffDto;
import com.goapi.goapi.domain.model.user.User;

public interface DatabaseTariffServiceFacade {
    boolean changeDatabaseTariff(User user, Integer dbId, Integer tariffId);
    DatabaseTariffDto getDatabaseTariff(User user, Integer dbId);
}
