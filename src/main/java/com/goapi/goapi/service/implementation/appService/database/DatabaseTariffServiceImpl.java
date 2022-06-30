package com.goapi.goapi.service.implementation.appService.database;

import com.goapi.goapi.domain.dto.appServiceobject.tariff.DatabaseTariffDto;
import com.goapi.goapi.domain.model.appService.tariff.DatabaseTariff;
import com.goapi.goapi.exception.tariff.database.DatabaseTariffNotFoundException;
import com.goapi.goapi.repo.appService.database.DatabaseTariffRepo;
import com.goapi.goapi.service.interfaces.appService.database.DatabaseTariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class DatabaseTariffServiceImpl implements DatabaseTariffService {

    private final DatabaseTariffRepo dbTariffRepo;

    @Override
    public DatabaseTariff getDatabaseTariffById(Integer tariffId) {
        Optional<DatabaseTariff> tariffOptional = dbTariffRepo.findById(tariffId);
        return tariffOptional.orElseThrow(() -> new DatabaseTariffNotFoundException(tariffId));
    }

    @Override
    public List<DatabaseTariffDto> listTariffs() {
        List<DatabaseTariff> databaseTariffs = dbTariffRepo.findAll();
        List<DatabaseTariffDto> databaseTariffDtoList = databaseTariffs.stream()
            .map(tariff -> new DatabaseTariffDto(
                tariff.getId(),
                tariff.getTariffName(),
                tariff.getCostPerDay(),
                tariff.getMaxSizeBytes())
            )
            .collect(Collectors.toList());
        return databaseTariffDtoList;
    }
}
