package com.goapi.goapi.service.interfaces.appService.database;

import com.goapi.goapi.domain.dto.tariff.DatabaseTariffDto;
import com.goapi.goapi.domain.model.appService.tariff.DatabaseTariff;

import java.util.List;

public interface DatabaseTariffService {
    DatabaseTariff getDatabaseTariffById(Integer id);

    List<DatabaseTariffDto> listTariffs();
}
