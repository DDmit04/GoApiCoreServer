package com.goapi.goapi.service.database;

import com.goapi.goapi.domain.database.DatabaseTariff;
import com.goapi.goapi.repo.database.DbatabaseTariffRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class DatabaseTariffService {

    private final DbatabaseTariffRepo dbTariffRepo;

    public Optional<DatabaseTariff> getDatabaseTariffById(Integer id) {
        return dbTariffRepo.findById(id);
    }

    public List<DatabaseTariff> listTariffs() {
        return dbTariffRepo.findAll();
    }
}
