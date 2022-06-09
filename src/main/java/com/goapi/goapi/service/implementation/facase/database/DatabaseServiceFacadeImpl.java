package com.goapi.goapi.service.implementation.facase.database;

import com.goapi.goapi.controller.forms.database.CreateDatabaseForm;
import com.goapi.goapi.domain.dto.database.DatabaseDto;
import com.goapi.goapi.domain.dto.database.DatabaseStatsDto;
import com.goapi.goapi.domain.model.bill.AppServiceBill;
import com.goapi.goapi.domain.model.database.Database;
import com.goapi.goapi.domain.model.database.DatabaseTariff;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.service.implementation.BillService;
import com.goapi.goapi.service.interfaces.database.DatabaseService;
import com.goapi.goapi.service.interfaces.database.DatabaseTariffService;
import com.goapi.goapi.service.interfaces.facase.database.DatabaseServiceFacade;
import com.goapi.goapi.service.interfaces.grpc.ExternalDatabaseService;
import lombok.RequiredArgsConstructor;
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
    private final BillService billService;

    @Override
    public DatabaseDto createNewDatabase(User owner, CreateDatabaseForm dbForm) {
        Integer tariffId = dbForm.getTariffId();
        DatabaseTariff tariff = databaseTariffService.getDatabaseTariffById(tariffId);
        AppServiceBill databaseAppServiceBill = billService.createDatabaseBill(owner);
        DatabaseDto databaseDto = databaseService.createNewDatabase(owner, tariff, databaseAppServiceBill, dbForm);
        return databaseDto;
    }

    @Override
    public DatabaseDto getDatabaseInfo(User user, Integer dbId) {
        Database db = getDatabaseCheckOwner(user, dbId);
        DatabaseStatsDto databaseStatsDto = externalDatabaseService.getDatabaseStats(dbId);
        return new DatabaseDto(
            db.getId(),
            db.getDatabaseName(),
            db.getCreatedAt(),
            db.getDatabaseType(),
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

    public AppServiceBill getDatabaseBill(User user, Integer dbId) {
        //TODO entity graph load bill
        Database db = getDatabaseCheckOwner(user, dbId);
        AppServiceBill appServiceBill = db.getAppServiceBill();
        return appServiceBill;
    }

    private Database getDatabaseCheckOwner(User user, Integer dbId) {
        Database db = databaseService.getDatabaseById(dbId);
        databaseService.isDatabaseOwnerOrThrow(user, db);
        return db;
    }

}
