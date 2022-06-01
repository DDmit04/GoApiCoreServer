package com.goapi.goapi.service.interfaces.database;

import com.goapi.goapi.domain.dto.tariff.DatabaseTariffDto;
import com.goapi.goapi.domain.model.database.DatabaseTariff;

import java.util.List;
import java.util.Optional;

public interface DatabaseTariffService {
    Optional<DatabaseTariff> getDatabaseTariffById(Integer id);

    List<DatabaseTariffDto> listTariffs();
}
