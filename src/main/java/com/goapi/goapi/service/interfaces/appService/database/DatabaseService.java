package com.goapi.goapi.service.interfaces.appService.database;

import com.goapi.goapi.controller.forms.database.CreateDatabaseRequest;
import com.goapi.goapi.domain.model.appService.database.Database;
import com.goapi.goapi.domain.model.appService.tariff.DatabaseTariff;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.user.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface DatabaseService {

    Database getDatabaseById(Integer id);
    Database getDatabaseByIdWithTariff(Integer databaseId);
    Database getDatabaseByIdWithOwner(Integer dbId);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Database createNewDatabase(User owner, DatabaseTariff tariff, AppServiceBill databaseAppServiceBill, CreateDatabaseRequest dbForm);
    void deleteDatabaseById(Integer databaseId);
    List<Database> listUserDatabases(User user);
    void setNewDatabasePassword(Database database, String newPassword);
    int getTotalUserDatabasesCount(User owner);
    void permitExternalDatabaseConnections(Database db);
    void forbidExternalDatabaseConnections(Database db);
}
