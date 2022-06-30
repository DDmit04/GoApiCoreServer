package com.goapi.goapi.service.implementation.facade.appService.database;

import com.example.DatabaseType;
import com.goapi.goapi.controller.forms.database.CreateDatabaseRequest;
import com.goapi.goapi.domain.dto.appServiceobject.AppServiceObjectStatusDto;
import com.goapi.goapi.domain.dto.appServiceobject.database.DatabaseDto;
import com.goapi.goapi.domain.dto.appServiceobject.database.DatabaseStatsDto;
import com.goapi.goapi.domain.dto.appServiceobject.database.SummaryDatabaseDto;
import com.goapi.goapi.domain.model.appService.AppServiceObjectStatus;
import com.goapi.goapi.domain.model.appService.database.Database;
import com.goapi.goapi.domain.model.appService.tariff.DatabaseTariff;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.appService.database.UserDatabasesCountCupException;
import com.goapi.goapi.service.interfaces.appService.AppServiceObjectService;
import com.goapi.goapi.service.interfaces.appService.database.DatabaseService;
import com.goapi.goapi.service.interfaces.appService.database.DatabaseTariffService;
import com.goapi.goapi.service.interfaces.facade.database.DatabaseServiceFacade;
import com.goapi.goapi.service.interfaces.facade.finances.PaymentsServiceFacade;
import com.goapi.goapi.service.interfaces.finances.bill.AppServiceBillService;
import com.goapi.goapi.service.interfaces.finances.bill.UserBillService;
import com.goapi.goapi.service.interfaces.grpc.ExternalDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class DatabaseServiceFacadeImpl implements DatabaseServiceFacade {

    private final DatabaseService databaseService;
    private final PaymentsServiceFacade paymentsServiceFacade;
    private final AppServiceObjectService appServiceObjectService;
    private final DatabaseTariffService databaseTariffService;
    private final ExternalDatabaseService externalDatabaseService;
    private final AppServiceBillService appServiceBillService;
    private final UserBillService userBillService;

    @Value("${limit.max-databases-count}")
    private int maxDatabasesCount;

    @Override
    public DatabaseDto createNewDatabase(User user, CreateDatabaseRequest createDatabaseRequest) {
        int totalUserDatabasesCount = databaseService.getTotalUserDatabasesCount(user);
        if (totalUserDatabasesCount < maxDatabasesCount) {
            Integer tariffId = createDatabaseRequest.getTariffId();
            DatabaseTariff tariff = databaseTariffService.getDatabaseTariffById(tariffId);
            AppServiceBill databaseBill = appServiceBillService.createDatabaseBill();
            Database newDatabase = databaseService.createNewDatabase(user, tariff, databaseBill, createDatabaseRequest);
            Integer newDbId = newDatabase.getId();
            DatabaseType databaseType = createDatabaseRequest.getDatabaseType();
            String newPassword = UUID.randomUUID().toString();
            long maxSizeBytes = tariff.getMaxSizeBytes();
            DatabaseStatsDto databaseStatsDto = externalDatabaseService.createExternalDatabase(maxSizeBytes, databaseType, newDbId, newPassword);
            paymentsServiceFacade.makeFirstAppServicePayment(user, newDatabase);
            DatabaseDto databaseDto = getDatabaseDto(newDatabase, databaseStatsDto);
            return databaseDto;
        }
        Integer userId = user.getId();
        throw new UserDatabasesCountCupException(userId);
    }

    @Override
    public DatabaseDto getDatabaseInfo(User user, Integer dbId) {
        Database database = getDatabaseCheckOwner(user, dbId);
        DatabaseStatsDto databaseStatsDto = externalDatabaseService.getDatabaseStats(dbId);
        return getDatabaseDto(database, databaseStatsDto);
    }

    @Override
    public String getDatabasePassword(User user, Integer dbId) {
        Database db = getDatabaseCheckOwner(user, dbId);
        return db.getDatabasePassword();
    }

    @Override
    public void generateNewDatabasePassword(User user, Integer dbId) {
        Database database = getDatabaseCheckOwner(user, dbId);
        String newPassword = UUID.randomUUID().toString();
        boolean passwordUpdated = externalDatabaseService.updateExternalDatabasePassword(dbId, newPassword);
        if (passwordUpdated) {
            databaseService.setNewDatabasePassword(database, newPassword);
        }
    }

    @Override
    public DatabaseDto resetDatabase(User user, Integer dbId) {
        Database database = getDatabaseCheckOwner(user, dbId);
        String newPassword = UUID.randomUUID().toString();
        DatabaseStatsDto databaseStatsDto = externalDatabaseService.resetExternalDatabase(dbId, newPassword);
        DatabaseDto newInfo = getDatabaseDto(database, databaseStatsDto);
        return newInfo;
    }

    @Override
    public boolean deleteDatabase(User user, Integer dbId) {
        getDatabaseCheckOwner(user, dbId);
        boolean deleted = externalDatabaseService.dropExternalDatabase(dbId);
        if (deleted) {
            databaseService.deleteDatabaseById(dbId);
        }
        return deleted;
    }

    @Override
    public void renameDatabase(User user, Integer dbId, String newDatabaseName) {
        Database db = getDatabaseCheckOwner(user, dbId);
        appServiceObjectService.renameAppServiceObjectApi(db, newDatabaseName);
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

    @Override
    public List<SummaryDatabaseDto> listUserDatabasesDtos(User user) {
        List<Database> databases = databaseService.listUserDatabases(user);
        List<SummaryDatabaseDto> summaryDatabaseDtoList = databases
            .stream()
            .map(database -> getSummaryDatabaseDto(database))
            .collect(Collectors.toList());
        return summaryDatabaseDtoList;
    }

    private DatabaseDto getDatabaseDto(Database database, DatabaseStatsDto databaseStatsDto) {
        AppServiceObjectStatus appServiceObjectStatus = database.getAppServiceObjectStatus();
        AppServiceObjectStatusDto appServiceObjectStatusDto = new AppServiceObjectStatusDto(appServiceObjectStatus);
        return new DatabaseDto(
            database.getId(),
            database.getName(),
            database.getCreatedAt(),
            appServiceObjectStatusDto,
            database.getDatabaseType(),
            databaseStatsDto
        );
    }

    private SummaryDatabaseDto getSummaryDatabaseDto(Database database) {
        AppServiceObjectStatusDto appServiceObjectStatusDto = new AppServiceObjectStatusDto(database.getAppServiceObjectStatus());
        return new SummaryDatabaseDto(
            database.getId(),
            database.getName(),
            database.getCreatedAt(),
            appServiceObjectStatusDto,
            database.getDatabaseType());
    }

    private Database getDatabaseCheckOwner(User user, Integer dbId) {
        Database db = databaseService.getDatabaseByIdWithOwner(dbId);
        appServiceObjectService.isAppServiceObjectOwnerOrThrow(user, db);
        return db;
    }

}
