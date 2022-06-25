package com.goapi.goapi.service.interfaces.facade.database;

import com.goapi.goapi.domain.dto.appServiceobject.tariff.DatabaseTariffDto;
import com.goapi.goapi.domain.model.user.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DatabaseTariffServiceFacade {

    void changeDatabaseTariff(User user, Integer dbId, Integer tariffId);
    DatabaseTariffDto getDatabaseTariff(User user, Integer dbId);
}
