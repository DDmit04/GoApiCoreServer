package com.goapi.goapi.service.interfaces.facade.database;

import com.goapi.goapi.controller.forms.database.CreateDatabaseRequest;
import com.goapi.goapi.domain.dto.appServiceobject.database.DatabaseDto;
import com.goapi.goapi.domain.dto.appServiceobject.database.SummaryDatabaseDto;
import com.goapi.goapi.domain.model.user.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface DatabaseServiceFacade {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    DatabaseDto createNewDatabase(User owner, CreateDatabaseRequest dbForm);
    DatabaseDto getDatabaseInfo(User user, Integer dbId);
    String getDatabasePassword(User user, Integer dbId);
    void generateNewDatabasePassword(User user, Integer dbId);
    DatabaseDto resetDatabase(User user, Integer dbId);
    boolean deleteDatabase(User user, Integer dbId);
    void renameDatabase(User user, Integer dbId, String newDatabaseName);
    void denyDatabaseExternalConnections(User user, Integer dbId);
    void allowDatabaseExternalConnections(User user, Integer dbId);
    List<SummaryDatabaseDto> listUserDatabasesDtos(User user);
}
