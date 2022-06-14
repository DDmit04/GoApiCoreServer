package com.goapi.goapi.service.implementation.facade.database;

import com.goapi.goapi.controller.forms.database.CreateDatabaseForm;
import com.goapi.goapi.domain.dto.database.DatabaseDto;
import com.goapi.goapi.domain.dto.database.DatabaseStatsDto;
import com.goapi.goapi.domain.model.appService.database.Database;
import com.goapi.goapi.domain.model.appService.tariff.DatabaseTariff;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.BillType;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.database.UserDatabasesCountCupException;
import com.goapi.goapi.service.interfaces.appService.database.DatabaseService;
import com.goapi.goapi.service.interfaces.appService.database.DatabaseTariffService;
import com.goapi.goapi.service.interfaces.facade.database.DatabaseServiceFacade;
import com.goapi.goapi.service.interfaces.grpc.ExternalDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class DatabaseServiceFacadeImpl implements DatabaseServiceFacade {

    private final DatabaseService databaseService;
    private final DatabaseTariffService databaseTariffService;
    private final ExternalDatabaseService externalDatabaseService;
    @Value("${limit.max-databases-count}")
    private int maxDatabasesCount;

    @Override
    public DatabaseDto createNewDatabase(User user, CreateDatabaseForm dbForm) {
        Integer tariffId = dbForm.getTariffId();
        DatabaseTariff tariff = databaseTariffService.getDatabaseTariffById(tariffId);
        AppServiceBill databaseBill = new AppServiceBill(user, BillType.DATABASE);
        int totalUserDatabasesCount = databaseService.getTotalUserDatabasesCount(user);
        if(totalUserDatabasesCount < maxDatabasesCount) {
            DatabaseDto databaseDto = databaseService.createNewDatabase(user, tariff, databaseBill, dbForm);
            return databaseDto;
        }
        Integer userId = user.getId();
        throw new UserDatabasesCountCupException(userId);
    }

    @Override
    public DatabaseDto getDatabaseInfo(User user, Integer dbId) {
        Database database = getDatabaseCheckOwner(user, dbId);
        boolean acceptExternalConnections = database.isAcceptExternalConnections();
        DatabaseStatsDto databaseStatsDto = externalDatabaseService.getDatabaseStats(dbId);
        return new DatabaseDto(
            database.getId(),
            database.getDatabaseName(),
            database.getCreatedAt(),
            database.getDatabaseType(),
            acceptExternalConnections,
            databaseStatsDto
        );
    }

    @Override
    public String getDatabasePassword(User user, Integer dbId) {
        Database db = getDatabaseCheckOwner(user, dbId);
        return db.getDatabasePassword();
    }

    @Override
    public boolean generateNewDbPassword(User user, Integer dbId) {
        Database db = getDatabaseCheckOwner(user, dbId);
        return databaseService.generateNewDbPassword(db);
    }

    @Override
    public DatabaseDto resetDatabase(User user, Integer dbId) {
        Database db = getDatabaseCheckOwner(user, dbId);
        DatabaseDto resetDatabaseDto = databaseService.resetDatabase(db);
        return resetDatabaseDto;
    }

    @Override
    public boolean deleteDatabase(User user, Integer dbId) {
        Database db = getDatabaseCheckOwner(user, dbId);
        boolean deleted = databaseService.deleteDatabase(db);
        return deleted;
    }

    @Override
    public boolean renameDatabase(User user, Integer dbId, String newDatabaseName) {
        Database db = getDatabaseCheckOwner(user, dbId);
        boolean renamed = databaseService.renameDatabase(db, newDatabaseName);
        return renamed;
    }

    @Override
    public void denyDatabaseExternalConnections(User user, Integer dbId) {
        Database db = getDatabaseCheckOwner(user, dbId);
        databaseService.forbidExternalDatabaseConnections(db);
        externalDatabaseService.forbidExternalDatabaseConnections(dbId);
    }

    @Override
    public void allowDatabaseExternalConnections(User user, Integer dbId) {
        Database db = getDatabaseCheckOwner(user, dbId);
        databaseService.permitExternalDatabaseConnections(db);
        externalDatabaseService.allowExternalDatabaseConnections(dbId);
    }

    private Database getDatabaseCheckOwner(User user, Integer dbId) {
        Database db = databaseService.getDatabaseByIdWithOwner(dbId);
        databaseService.isDatabaseOwnerOrThrow(user, db);
        return db;
    }

}
