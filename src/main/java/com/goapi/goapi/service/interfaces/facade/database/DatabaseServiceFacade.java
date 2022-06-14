package com.goapi.goapi.service.interfaces.facade.database;

import com.goapi.goapi.controller.forms.database.CreateDatabaseForm;
import com.goapi.goapi.domain.dto.database.DatabaseDto;
import com.goapi.goapi.domain.model.user.User;

public interface DatabaseServiceFacade {
    DatabaseDto createNewDatabase(User owner, CreateDatabaseForm dbForm);

    DatabaseDto getDatabaseInfo(User user, Integer dbId);

    String getDatabasePassword(User user, Integer dbId);

    boolean generateNewDbPassword(User user, Integer dbId);

    DatabaseDto resetDatabase(User user, Integer dbId);

    boolean deleteDatabase(User user, Integer dbId);

    boolean renameDatabase(User user, Integer dbId, String newDatabaseName);

    void denyDatabaseExternalConnections(User user, Integer dbId);

    void allowDatabaseExternalConnections(User user, Integer dbId);
}
