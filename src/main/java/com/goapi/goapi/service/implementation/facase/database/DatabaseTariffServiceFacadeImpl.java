package com.goapi.goapi.service.implementation.facase.database;

import com.goapi.goapi.domain.dto.database.DatabaseStatsDto;
import com.goapi.goapi.domain.dto.tariff.DatabaseTariffDto;
import com.goapi.goapi.domain.model.database.Database;
import com.goapi.goapi.domain.model.database.DatabaseTariff;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.database.DatabaseNotFoundException;
import com.goapi.goapi.exception.tariff.database.DatabaseTariffChangeException;
import com.goapi.goapi.exception.tariff.database.DatabaseTariffConditionChangeException;
import com.goapi.goapi.exception.tariff.database.DatabaseTariffNotFoundException;
import com.goapi.goapi.service.interfaces.database.DatabaseService;
import com.goapi.goapi.service.interfaces.database.DatabaseTariffService;
import com.goapi.goapi.service.interfaces.facase.database.DatabaseTariffServiceFacade;
import com.goapi.goapi.service.interfaces.grpc.ExternalDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class DatabaseTariffServiceFacadeImpl implements DatabaseTariffServiceFacade {

    private final DatabaseService databaseService;
    private final DatabaseTariffService databaseTariffService;
    private final ExternalDatabaseService externalDatabaseService;

    @Override
    public boolean changeDatabaseTariff(User user, Integer dbId, Integer tariffId) {
        Optional<Database> databaseOptional = databaseService.getDatabaseById(dbId);
        Optional<DatabaseTariff> tariffOptional = databaseTariffService.getDatabaseTariffById(tariffId);
        return databaseOptional.map(db -> {
            return tariffOptional.map(tariff -> {
                DatabaseTariff currentTariff = db.getDbTariff();
                Integer newTariffId = tariff.getId();
                Integer currentTariffId = currentTariff.getId();
                long maxSizeBytes = tariff.getMaxSizeBytes();
                Integer id = db.getId();
                databaseService.isDatabaseOwnerOrThrow(user, db);
                boolean canChangeTariff = checkChangeDatabaseTariffCondition(db, tariff);
                boolean changed = false;
                if (canChangeTariff) {
                    changed = externalDatabaseService.changeExternalDatabaseTariff(id, maxSizeBytes);
                    if (changed) {
                        databaseService.changeDatabaseTariff(db, tariff);
                        return true;
                    }
                    throw new DatabaseTariffChangeException(dbId, newTariffId, currentTariffId);
                }
                throw new DatabaseTariffConditionChangeException(dbId, newTariffId, currentTariffId);
            }).orElseThrow(() -> new DatabaseTariffNotFoundException(tariffId));
        }).orElseThrow(() -> new DatabaseNotFoundException(dbId));
    }

    @Override
    public DatabaseTariffDto getDatabaseTariff(User user, Integer dbId) {
        Database db = databaseService.getDatabaseCheckOwner(user, dbId);
        DatabaseTariff dbTariff = db.getDbTariff();
        DatabaseTariffDto databaseTariffDto = new DatabaseTariffDto(
            dbTariff.getId(),
            dbTariff.getTariff_name(),
            dbTariff.getCostPerMonth(),
            dbTariff.getMaxSizeBytes()
        );
        return databaseTariffDto;
    }

    private boolean checkChangeDatabaseTariffCondition(Database database, DatabaseTariff newTariff) {
        Integer dbId = database.getId();
        DatabaseTariff currentTariff = database.getDbTariff();
        if (!currentTariff.equals(newTariff)) {
            DatabaseStatsDto databaseStats = externalDatabaseService.getDatabaseStats(dbId);
            long newMaxSizeBytes = newTariff.getMaxSizeBytes();
            long currentSize = databaseStats.getCurrentSize();
            boolean canChange = currentSize >= newMaxSizeBytes;
            return canChange;
        }
        return false;
    }

}
