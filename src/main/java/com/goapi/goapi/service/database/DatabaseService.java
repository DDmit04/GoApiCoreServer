package com.goapi.goapi.service.database;

import com.example.DatabaseType;
import com.goapi.goapi.controller.dto.DatabaseAuthInfo;
import com.goapi.goapi.controller.dto.DatabaseInfoDTO;
import com.goapi.goapi.controller.form.database.CreateDatabaseForm;
import com.goapi.goapi.domain.database.Database;
import com.goapi.goapi.domain.database.DatabaseTariff;
import com.goapi.goapi.domain.user.User;
import com.goapi.goapi.repo.database.DatabaseRepo;
import com.goapi.goapi.service.grpc.ExternalDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class DatabaseService {

    private final ExternalDatabaseService externalDatabaseService;

    private final DatabaseRepo databaseRepo;

    public Optional<Database> getDatabaseById(Integer id) {
        return databaseRepo.findById(id);
    }

    public List<Database> listUserDatabases(User user) {
        return databaseRepo.findAllByOwnerId(user.getId());
    }

    public DatabaseAuthInfo createNewDatabase(User owner, DatabaseTariff tariff, CreateDatabaseForm dbForm) {
        String newPassword = UUID.randomUUID().toString();
        DatabaseType databaseType = dbForm.getDatabaseType();
        Date createdAt = new Date();
        BigDecimal moneyAmount = BigDecimal.ZERO;

        Database newDb = new Database("tmpName", "tmpUsername", createdAt, newPassword, moneyAmount, databaseType, tariff, owner);
        newDb = databaseRepo.save(newDb);

        Integer newDbId = newDb.getId();
        DatabaseAuthInfo databaseAuthInfo = externalDatabaseService.createExternalDatabase(tariff, databaseType, newDbId, newPassword);

        String dbName = databaseAuthInfo.dbName();
        String username = databaseAuthInfo.username();
        newDb.setName(dbName);
        newDb.setUsername(username);
        databaseRepo.save(newDb);

        return databaseAuthInfo;
    }

    public boolean deleteDatabase(Database database) {
        boolean deleted;
        Integer dbId = database.getId();
        deleted = externalDatabaseService.dropExternalDatabase(dbId);
        if (deleted) {
            databaseRepo.deleteById(database.getId());
        }
        return deleted;
    }

    public DatabaseInfoDTO resetDatabase(Database database) {
        String newPassword = UUID.randomUUID().toString();
        DatabaseAuthInfo authInfo = externalDatabaseService.resetExternalDatabase(database.getId(), newPassword);
        long dbSize = database.getDbTariff().getMaxSizeBytes();
        DatabaseInfoDTO newInfo = new DatabaseInfoDTO(authInfo, database, 0, dbSize);
        return newInfo;
    }

    public boolean generateNewDbPassword(Database database) {
        boolean passwordUpdated;
        String newPassword = UUID.randomUUID().toString();
        Integer dbId = database.getId();
        passwordUpdated = externalDatabaseService.updateExternalDatabasePassword(dbId, newPassword);
        if (passwordUpdated) {
            database.setPassword(newPassword);
            databaseRepo.save(database);
        }
        return passwordUpdated;
    }

    public boolean changeDatabaseTariff(Database database, DatabaseTariff tariff) {
        if (!database.getDbTariff().equals(tariff)) {
            Integer dbId = database.getId();
            boolean changed = externalDatabaseService.changeExternalDatabaseTariff(dbId, tariff.getMaxSizeBytes());
            if (changed) {
                database.setDbTariff(tariff);
                databaseRepo.save(database);
                return true;
            }
        } else {
            throw new RuntimeException();
        }
        throw new RuntimeException();
    }

}
