package com.goapi.goapi.service.facase;

import com.goapi.goapi.controller.dto.DatabaseAuthInfo;
import com.goapi.goapi.controller.dto.DatabaseInfoDTO;
import com.goapi.goapi.controller.form.database.CreateDatabaseForm;
import com.goapi.goapi.domain.database.Database;
import com.goapi.goapi.domain.database.DatabaseTariff;
import com.goapi.goapi.domain.user.User;
import com.goapi.goapi.service.database.DatabaseService;
import com.goapi.goapi.service.database.DatabaseTariffService;
import com.goapi.goapi.service.grpc.ExternalDatabaseLocationService;
import com.goapi.goapi.service.grpc.ExternalDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class DatabaseServiceFacade {

    private final DatabaseService databaseService;
    private final DatabaseTariffService databaseTariffService;
    private final ExternalDatabaseService externalDatabaseService;
    private final ExternalDatabaseLocationService externalDatabaseLocationService;

    public Optional<DatabaseAuthInfo> createNewDatabase(User owner, CreateDatabaseForm dbForm) {
        Optional<DatabaseTariff> tariffOptional = databaseTariffService.getDatabaseTariffById(dbForm.getTariffId());
        if (tariffOptional.isPresent() && owner != null) {
            DatabaseTariff tariff = tariffOptional.get();
            DatabaseAuthInfo dbInfo = databaseService.createNewDatabase(owner, tariff, dbForm);
            return Optional.ofNullable(dbInfo);
        } else if (!tariffOptional.isPresent()) {
            throw new RuntimeException();
        }
        throw new RuntimeException();
    }

    public boolean changeDatabaseTariff(User user, Integer dbId, Integer tariffId) {
        Optional<Database> databaseOptional = databaseService.getDatabaseById(dbId);
        Optional<DatabaseTariff> tariffOptional = databaseTariffService.getDatabaseTariffById(tariffId);
        if (tariffOptional.isPresent() && databaseOptional.isPresent() && user != null) {
            DatabaseTariff tariff = tariffOptional.get();
            Database db = databaseOptional.get();
            isDatabaseOwnerOrThrow(user, db);
            return databaseService.changeDatabaseTariff(db, tariff);
        } else if (!tariffOptional.isPresent()) {
            throw new RuntimeException();
        } else if (!databaseOptional.isPresent()) {
            throw new RuntimeException();
        }
        throw new RuntimeException();
    }

    public DatabaseInfoDTO getDatabaseInfo(User user, Integer dbId) {
        Optional<Database> databaseOptional = databaseService.getDatabaseById(dbId);
        if (databaseOptional.isPresent() && user != null) {
            Database db = databaseOptional.get();
            isDatabaseOwnerOrThrow(user, db);
            DatabaseTariff tariff = db.getDbTariff();
            String dbHost = externalDatabaseLocationService.getDatabaseLocation(dbId);
            return new DatabaseInfoDTO(dbHost, db, 0, tariff.getMaxSizeBytes());
        } else if (!databaseOptional.isPresent()) {
            throw new RuntimeException();
        }
        throw new RuntimeException();
    }

    public String getDatabasePassword(User user, Integer dbId) {
        Optional<Database> databaseOptional = databaseService.getDatabaseById(dbId);
        if (databaseOptional.isPresent() && user != null) {
            Database db = databaseOptional.get();
            isDatabaseOwnerOrThrow(user, db);
            return db.getPassword();
        } else if (!databaseOptional.isPresent()) {
            throw new RuntimeException();
        }
        throw new RuntimeException();
    }

    public boolean generateNewDbPassword(User user, Integer dbId) {
        Optional<Database> databaseOptional = databaseService.getDatabaseById(dbId);
        if (databaseOptional.isPresent() && user != null) {
            Database db = databaseOptional.get();
            isDatabaseOwnerOrThrow(user, db);
            return databaseService.generateNewDbPassword(db);
        } else if (!databaseOptional.isPresent()) {
            throw new RuntimeException();
        }
        throw new RuntimeException();
    }

    public DatabaseInfoDTO resetDatabase(User user, Integer dbId) {
        Optional<Database> databaseOptional = databaseService.getDatabaseById(dbId);
        if (databaseOptional.isPresent() && user != null) {
            Database db = databaseOptional.get();
            isDatabaseOwnerOrThrow(user, db);
            return databaseService.resetDatabase(db);
        } else if (!databaseOptional.isPresent()) {
            throw new RuntimeException();
        }
        throw new RuntimeException();
    }

    public boolean deleteDatabase(User user, Integer dbId) {
        Optional<Database> databaseOptional = databaseService.getDatabaseById(dbId);
        if (databaseOptional.isPresent() && user != null) {
            Database db = databaseOptional.get();
            isDatabaseOwnerOrThrow(user, db);
            return databaseService.deleteDatabase(db);
        } else if (!databaseOptional.isPresent()) {
            throw new RuntimeException();
        }
        throw new RuntimeException();
    }

    public DatabaseTariff getDatabaseTariff(User user, Integer dbId) {
        Optional<Database> databaseOptional = databaseService.getDatabaseById(dbId);
        if (databaseOptional.isPresent() && user != null) {
            Database db = databaseOptional.get();
            isDatabaseOwnerOrThrow(user, db);
            return db.getDbTariff();
        } else if (!databaseOptional.isPresent()) {
            throw new RuntimeException();
        }
        throw new RuntimeException();
    }

    protected boolean isDatabaseOwnerOrThrow(User user, Database database) {
        boolean isOwner = database.getOwner().equals(user);
        if (isOwner) {
            return true;
        }
        throw new RuntimeException();
    }
}
