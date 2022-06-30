package com.goapi.goapi.service.implementation.appService.database;

import com.example.DatabaseType;
import com.goapi.goapi.controller.forms.database.CreateDatabaseRequest;
import com.goapi.goapi.domain.model.appService.database.Database;
import com.goapi.goapi.domain.model.appService.tariff.DatabaseTariff;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.appService.database.DatabaseNotFoundException;
import com.goapi.goapi.repo.appService.database.DatabaseRepo;
import com.goapi.goapi.service.interfaces.appService.database.DatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class DatabaseServiceImpl implements DatabaseService {

    private final DatabaseRepo databaseRepo;

    @Override
    public Database getDatabaseById(Integer databaseId) {
        Optional<Database> databaseOptional = databaseRepo.findById(databaseId);
        return databaseOptional.orElseThrow(() -> new DatabaseNotFoundException(databaseId));
    }

    @Override
    public Database getDatabaseByIdWithOwner(Integer databaseId) {
        Optional<Database> databaseOptional = databaseRepo.findDatabaseByIdWithOwner(databaseId);
        return databaseOptional.orElseThrow(() -> new DatabaseNotFoundException(databaseId));
    }

    @Override
    public Database getDatabaseByIdWithTariff(Integer databaseId) {
        Optional<Database> databaseOptional = databaseRepo.findByIdWithTariff(databaseId);
        return databaseOptional.orElseThrow(() -> new DatabaseNotFoundException(databaseId));
    }

    @Override
    public int getTotalUserDatabasesCount(User owner) {
        Integer userId = owner.getId();
        return databaseRepo.countByOwner_Id(userId);
    }

    @Override
    public void permitExternalDatabaseConnections(Database db) {
        updateAllowExternalDatabaseConnections(db, true);
    }

    @Override
    public void forbidExternalDatabaseConnections(Database db) {
        updateAllowExternalDatabaseConnections(db, false);
    }

    @Override
    public List<Database> listUserDatabases(User user) {
        Integer userId = user.getId();
        List<Database> databases = databaseRepo.findAllDatabasesByOwnerId(userId);
        return databases;
    }

    @Override
    public Database createNewDatabase(User owner, DatabaseTariff tariff, AppServiceBill databaseAppServiceBill, CreateDatabaseRequest dbForm) {
        String newPassword = UUID.randomUUID().toString();
        DatabaseType databaseType = dbForm.getDatabaseType();
        String externalName = dbForm.getDbName();
        Database newDb = new Database(owner, databaseAppServiceBill, externalName, newPassword, databaseType, tariff);
        newDb = databaseRepo.save(newDb);
        return newDb;
    }

    @Override
    public void deleteDatabaseById(Integer databaseId) {
        databaseRepo.deleteById(databaseId);
    }

    @Override
    public void setNewDatabasePassword(Database database, String newPassword) {
            database.setDatabasePassword(newPassword);
            databaseRepo.save(database);
    }

    private void updateAllowExternalDatabaseConnections(Database db, boolean allow) {
        boolean acceptExternalConnections = db.isAcceptExternalConnections();
        if (acceptExternalConnections != allow) {
            db.setAcceptExternalConnections(allow);
            databaseRepo.save(db);
        }
    }

}
