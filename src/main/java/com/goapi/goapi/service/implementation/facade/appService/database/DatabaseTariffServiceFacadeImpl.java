package com.goapi.goapi.service.implementation.facade.appService.database;

import com.goapi.goapi.domain.dto.appServiceobject.database.DatabaseStatsDto;
import com.goapi.goapi.domain.dto.appServiceobject.tariff.DatabaseTariffDto;
import com.goapi.goapi.domain.model.appService.database.Database;
import com.goapi.goapi.domain.model.appService.tariff.DatabaseTariff;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.tariff.database.DatabaseTariffChangeException;
import com.goapi.goapi.exception.tariff.database.DatabaseTariffConditionChangeException;
import com.goapi.goapi.service.interfaces.appService.AppServiceObjectService;
import com.goapi.goapi.service.interfaces.appService.database.DatabaseService;
import com.goapi.goapi.service.interfaces.appService.database.DatabaseTariffService;
import com.goapi.goapi.service.interfaces.facade.database.DatabaseTariffServiceFacade;
import com.goapi.goapi.service.interfaces.facade.finances.PaymentsServiceFacade;
import com.goapi.goapi.service.interfaces.grpc.ExternalDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class DatabaseTariffServiceFacadeImpl implements DatabaseTariffServiceFacade {

    private final DatabaseService databaseService;
    private final AppServiceObjectService appServiceObjectService;
    private final DatabaseTariffService databaseTariffService;
    private final ExternalDatabaseService externalDatabaseService;
    private final PaymentsServiceFacade paymentsServiceFacade;

    @Override
    public void changeDatabaseTariff(User user, Integer dbId, Integer tariffId) {
        DatabaseTariff newTariff = databaseTariffService.getDatabaseTariffById(tariffId);
        Database targetDatabase = getDatabaseWithTariffCheckOwner(user, dbId);
        Integer targetDatabaseId = targetDatabase.getId();
        DatabaseTariff currentTariff = targetDatabase.getAppServiceTariff();
        Integer newTariffId = newTariff.getId();
        Integer currentTariffId = currentTariff.getId();
        long maxSizeBytes = newTariff.getMaxSizeBytes();
        boolean canChangeTariff = checkChangeDatabaseTariffCondition(targetDatabase, newTariff);
        if (canChangeTariff) {
            boolean changed = externalDatabaseService.changeExternalDatabaseTariff(targetDatabaseId, maxSizeBytes);
            if (changed) {
                appServiceObjectService.changeAppServiceObjectTariff(targetDatabase, newTariff);
                paymentsServiceFacade.makeFirstAppServicePaymentAfterTariffChange(user, targetDatabase);
                return;
            }
            throw new DatabaseTariffChangeException(targetDatabaseId, newTariffId, currentTariffId);
        }
        throw new DatabaseTariffConditionChangeException(targetDatabaseId, newTariffId, currentTariffId);
    }

    @Override
    public DatabaseTariffDto getDatabaseTariff(User user, Integer dbId) {
        Database db = getDatabaseWithTariffCheckOwner(user, dbId);
        DatabaseTariff dbTariff = db.getAppServiceTariff();
        DatabaseTariffDto databaseTariffDto = new DatabaseTariffDto(
            dbTariff.getId(),
            dbTariff.getTariffName(),
            dbTariff.getCostPerDay(),
            dbTariff.getMaxSizeBytes()
        );
        return databaseTariffDto;
    }

    private boolean checkChangeDatabaseTariffCondition(Database database, DatabaseTariff newTariff) {
        Integer dbId = database.getId();
        DatabaseTariff currentTariff = database.getAppServiceTariff();
        if (!currentTariff.equals(newTariff)) {
            DatabaseStatsDto databaseStats = externalDatabaseService.getDatabaseStats(dbId);
            long newMaxSizeBytes = newTariff.getMaxSizeBytes();
            long currentSize = databaseStats.getCurrentSize();
            boolean canChange = currentSize <= newMaxSizeBytes;
            return canChange;
        }
        return false;
    }

    private Database getDatabaseWithTariffCheckOwner(User user, Integer databaseId) {
        Database database = databaseService.getDatabaseByIdWithTariff(databaseId);
        appServiceObjectService.isAppServiceObjectOwnerOrThrow(user, database);
        return database;
    }

}
