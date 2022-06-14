package com.goapi.goapi.service.implementation.appService.database;

import com.example.DatabaseType;
import com.goapi.goapi.controller.forms.database.CreateDatabaseForm;
import com.goapi.goapi.domain.dto.database.DatabaseDto;
import com.goapi.goapi.domain.dto.database.DatabaseStatsDto;
import com.goapi.goapi.domain.dto.database.SummaryDatabaseDto;
import com.goapi.goapi.domain.model.appService.database.Database;
import com.goapi.goapi.domain.model.appService.tariff.DatabaseTariff;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.database.DatabaseNotFoundException;
import com.goapi.goapi.exception.database.DatabaseOwnerException;
import com.goapi.goapi.repo.database.DatabaseRepo;
import com.goapi.goapi.service.interfaces.appService.database.DatabaseService;
import com.goapi.goapi.service.interfaces.grpc.ExternalDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class DatabaseServiceImpl implements DatabaseService {

    private final ExternalDatabaseService externalDatabaseService;

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
        Optional<Database> databaseOptional = databaseRepo.findById(databaseId);
        return databaseOptional.orElseThrow(() -> new DatabaseNotFoundException(databaseId));
    }

    @Override
    public int getTotalUserDatabasesCount(User owner) {
        Integer userId = owner.getId();
        return databaseRepo.getTotalUserDatabasesCount(userId);
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
    public List<SummaryDatabaseDto> listUserDatabases(User user) {
        Integer userId = user.getId();
        List<Database> databases = databaseRepo.findAllDatabasesByOwnerId(userId);
        List<SummaryDatabaseDto> summaryDatabaseDtoList = databases
            .stream()
            .map(database -> new SummaryDatabaseDto(
                database.getId(),
                database.getDatabaseName(),
                database.getCreatedAt(),
                database.getDatabaseType())
            )
            .collect(Collectors.toList());
        return summaryDatabaseDtoList;
    }

    @Override
    public DatabaseDto createNewDatabase(User owner, DatabaseTariff tariff, AppServiceBill databaseAppServiceBill, CreateDatabaseForm dbForm) {
        String newPassword = UUID.randomUUID().toString();
        DatabaseType databaseType = dbForm.getDatabaseType();
        String externalName = dbForm.getDbName();

        Database newDb = new Database(owner, databaseAppServiceBill, externalName, newPassword, databaseType, tariff);
        newDb = databaseRepo.save(newDb);

        Integer newDbId = newDb.getId();
        long maxSizeBytes = tariff.getMaxSizeBytes();
        DatabaseStatsDto databaseStatsDto = externalDatabaseService.createExternalDatabase(maxSizeBytes, databaseType, newDbId, newPassword);
        Database database = databaseRepo.save(newDb);
        newDbId = database.getId();
        Date createdAt = database.getCreatedAt();
        databaseType = database.getDatabaseType();
        boolean acceptExternalConnections = database.isAcceptExternalConnections();
        DatabaseDto databaseDto = new DatabaseDto(newDbId, externalName, createdAt, databaseType, acceptExternalConnections, databaseStatsDto);
        return databaseDto;
    }

    @Override
    public boolean deleteDatabase(Database database) {
        boolean deleted;
        Integer dbId = database.getId();
        deleted = externalDatabaseService.dropExternalDatabase(dbId);
        if (deleted) {
            databaseRepo.deleteById(database.getId());
        }
        return deleted;
    }

    @Override
    public DatabaseDto resetDatabase(Database database) {
        String newPassword = UUID.randomUUID().toString();
        Integer dbId = database.getId();
        boolean acceptExternalConnections = database.isAcceptExternalConnections();
        DatabaseStatsDto databaseStatsDto = externalDatabaseService.resetExternalDatabase(dbId, newPassword);
        DatabaseDto newInfo = new DatabaseDto(
            dbId,
            database.getDatabaseName(),
            database.getCreatedAt(),
            database.getDatabaseType(),
            acceptExternalConnections,
            databaseStatsDto
        );
        return newInfo;
    }

    @Override
    public boolean generateNewDbPassword(Database database) {
        boolean passwordUpdated;
        String newPassword = UUID.randomUUID().toString();
        Integer dbId = database.getId();
        passwordUpdated = externalDatabaseService.updateExternalDatabasePassword(dbId, newPassword);
        if (passwordUpdated) {
            database.setDatabasePassword(newPassword);
            databaseRepo.save(database);
        }
        return passwordUpdated;
    }

    @Override
    public void changeDatabaseTariff(Database database, DatabaseTariff newTariff) {
        database.setDbTariff(newTariff);
        databaseRepo.save(database);
    }

    @Override
    public boolean isDatabaseOwnerOrThrow(User user, Database database) {
        boolean isOwner = database.getOwner().equals(user);
        if (isOwner) {
            return true;
        }
        throw new DatabaseOwnerException(database.getId());
    }

    @Override
    public boolean renameDatabase(Database db, String newDatabaseName) {
        db.setDatabaseName(newDatabaseName);
        databaseRepo.save(db);
        return true;
    }

    private void updateAllowExternalDatabaseConnections(Database db, boolean allow) {
        boolean acceptExternalConnections = db.isAcceptExternalConnections();
        if (acceptExternalConnections != allow) {
            db.setAcceptExternalConnections(allow);
            databaseRepo.save(db);
        }
    }

}
