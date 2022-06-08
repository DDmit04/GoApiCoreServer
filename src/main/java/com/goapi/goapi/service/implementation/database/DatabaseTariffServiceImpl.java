package com.goapi.goapi.service.implementation.database;

import com.goapi.goapi.domain.dto.tariff.DatabaseTariffDto;
import com.goapi.goapi.domain.model.database.DatabaseTariff;
import com.goapi.goapi.repo.database.DbatabaseTariffRepo;
import com.goapi.goapi.service.interfaces.database.DatabaseTariffService;
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

    private final DbatabaseTariffRepo dbTariffRepo;

    @Override
    public Optional<DatabaseTariff> getDatabaseTariffById(Integer id) {
        return dbTariffRepo.findById(id);
    }

    @Override
    public List<DatabaseTariffDto> listTariffs() {
        List<DatabaseTariff> databaseTariffs = dbTariffRepo.findAll();
        List<DatabaseTariffDto> databaseTariffDtoList = databaseTariffs.stream()
            .map(tariff -> new DatabaseTariffDto(
                tariff.getId(),
                tariff.getTariff_name(),
                tariff.getCostPerMonth(),
                tariff.getMaxSizeBytes())
            )
            .collect(Collectors.toList());
        return databaseTariffDtoList;
    }
}
