package com.goapi.goapi.service.implementation.facase;

import com.goapi.goapi.controller.form.database.CreateDatabaseForm;
import com.goapi.goapi.domain.dto.database.DatabaseDto;
import com.goapi.goapi.domain.dto.database.DatabaseStatsDto;
import com.goapi.goapi.domain.model.database.Database;
import com.goapi.goapi.domain.model.database.DatabaseTariff;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.tariff.database.DatabaseTariffNotFoundException;
import com.goapi.goapi.service.interfaces.database.DatabaseService;
import com.goapi.goapi.service.interfaces.database.DatabaseTariffService;
import com.goapi.goapi.service.interfaces.facase.DatabaseServiceFacade;
import com.goapi.goapi.service.interfaces.grpc.ExternalDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class DatabaseServiceFacadeImpl implements DatabaseServiceFacade {

    private final DatabaseService databaseService;
    private final DatabaseTariffService databaseTariffService;
    private final ExternalDatabaseService externalDatabaseService;

    @Override
    public DatabaseDto createNewDatabase(User owner, CreateDatabaseForm dbForm) {
        Integer tariffId = dbForm.getTariffId();
        Optional<DatabaseTariff> tariffOptional = databaseTariffService.getDatabaseTariffById(tariffId);
        return tariffOptional.map(tariff -> {
            DatabaseDto databaseDto = databaseService.createNewDatabase(owner, tariff, dbForm);
            return databaseDto;
        }).orElseThrow(() -> new DatabaseTariffNotFoundException(tariffId));
    }

    @Override
    public DatabaseDto getDatabaseInfo(User user, Integer dbId) {
        Database db = databaseService.getDatabaseCheckOwner(user, dbId);
        DatabaseStatsDto databaseStatsDto = externalDatabaseService.getDatabaseStats(dbId);
        return new DatabaseDto(
            db.getId(),
            db.getName(),
            db.getCreatedAt(),
            db.getDatabaseType(),
            databaseStatsDto
        );
    }

    @Override
    public String getDatabasePassword(User user, Integer dbId) {
        Database db = databaseService.getDatabaseCheckOwner(user, dbId);
        return db.getPassword();
    }

    @Override
    public boolean generateNewDbPassword(User user, Integer dbId) {
        Database db = databaseService.getDatabaseCheckOwner(user, dbId);
        return databaseService.generateNewDbPassword(db);
    }

    @Override
    public DatabaseDto resetDatabase(User user, Integer dbId) {
        Database db = databaseService.getDatabaseCheckOwner(user, dbId);
        DatabaseDto resetDatabaseDto = databaseService.resetDatabase(db);
        return resetDatabaseDto;
    }

    @Override
    public boolean deleteDatabase(User user, Integer dbId) {
        Database db = databaseService.getDatabaseCheckOwner(user, dbId);
        boolean deleted = databaseService.deleteDatabase(db);
        return deleted;
    }

    @Override
    public boolean renameDatabase(User user, Integer dbId, String newDatabaseName) {
        Database db = databaseService.getDatabaseCheckOwner(user, dbId);
        boolean renamed = databaseService.renameDatabase(db, newDatabaseName);
        return renamed;
    }

}
