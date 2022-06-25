package com.goapi.goapi.service.interfaces.appService.database;

import com.goapi.goapi.domain.dto.appServiceobject.tariff.DatabaseTariffDto;
import com.goapi.goapi.domain.model.appService.tariff.DatabaseTariff;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface DatabaseTariffService {

    DatabaseTariff getDatabaseTariffById(Integer id);
    List<DatabaseTariffDto> listTariffs();
}
