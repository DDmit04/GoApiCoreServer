package com.goapi.goapi.service.interfaces.appService.database;

import com.goapi.goapi.controller.forms.database.CreateDatabaseForm;
import com.goapi.goapi.domain.dto.database.DatabaseDto;
import com.goapi.goapi.domain.dto.database.SummaryDatabaseDto;
import com.goapi.goapi.domain.model.appService.database.Database;
import com.goapi.goapi.domain.model.appService.tariff.DatabaseTariff;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.user.User;

import java.util.List;

public interface DatabaseService {
    Database getDatabaseById(Integer id);

    List<SummaryDatabaseDto> listUserDatabases(User user);

    DatabaseDto createNewDatabase(User owner, DatabaseTariff tariff, AppServiceBill databaseAppServiceBill, CreateDatabaseForm dbForm);

    boolean deleteDatabase(Database database);

    DatabaseDto resetDatabase(Database database);

    boolean generateNewDbPassword(Database database);

    void changeDatabaseTariff(Database database, DatabaseTariff tariff);

    boolean isDatabaseOwnerOrThrow(User user, Database database);

    boolean renameDatabase(Database db, String newDatabaseName);

    Database getDatabaseByIdWithTariff(Integer databaseId);

    Database getDatabaseByIdWithOwner(Integer dbId);

    int getTotalUserDatabasesCount(User owner);

    void permitExternalDatabaseConnections(Database db);

    void forbidExternalDatabaseConnections(Database db);
}
