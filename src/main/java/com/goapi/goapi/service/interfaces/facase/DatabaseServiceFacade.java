package com.goapi.goapi.service.interfaces.facase;

import com.goapi.goapi.controller.form.database.CreateDatabaseForm;
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
}
