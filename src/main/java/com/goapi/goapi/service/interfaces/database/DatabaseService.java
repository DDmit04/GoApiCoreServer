package com.goapi.goapi.service.interfaces.database;

import com.goapi.goapi.controller.forms.database.CreateDatabaseForm;
import com.goapi.goapi.domain.dto.database.DatabaseDto;
import com.goapi.goapi.domain.dto.database.SummaryDatabaseDto;
import com.goapi.goapi.domain.model.bill.Bill;
import com.goapi.goapi.domain.model.database.Database;
import com.goapi.goapi.domain.model.database.DatabaseTariff;
import com.goapi.goapi.domain.model.user.User;

import java.util.List;
import java.util.Optional;

public interface DatabaseService {
    Optional<Database> getDatabaseById(Integer id);

    List<SummaryDatabaseDto> listUserDatabases(User user);

    DatabaseDto createNewDatabase(User owner, DatabaseTariff tariff, Bill databaseBill, CreateDatabaseForm dbForm);

    boolean deleteDatabase(Database database);

    DatabaseDto resetDatabase(Database database);

    boolean generateNewDbPassword(Database database);

    void changeDatabaseTariff(Database database, DatabaseTariff tariff);

    Database getDatabaseCheckOwner(User user, Integer dbId);

    boolean isDatabaseOwnerOrThrow(User user, Database database);

    boolean renameDatabase(Database db, String newDatabaseName);
}
