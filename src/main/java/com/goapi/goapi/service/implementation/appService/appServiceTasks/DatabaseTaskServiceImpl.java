package com.goapi.goapi.service.implementation.appService.appServiceTasks;

import com.goapi.goapi.domain.model.appService.AppServiceObject;
import com.goapi.goapi.props.appServiceTimeProps.AppServiceTimeProps;
import com.goapi.goapi.service.interfaces.appService.AppServiceObjectService;
import com.goapi.goapi.service.interfaces.appService.appServiceTasks.AppServiceSchedulerService;
import com.goapi.goapi.service.interfaces.appService.database.DatabaseService;
import com.goapi.goapi.service.interfaces.grpc.ExternalDatabaseService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@Qualifier("DatabaseTaskService")
public class DatabaseTaskServiceImpl extends CommonApiTaskService {

    private final DatabaseService databaseService;
    private final ExternalDatabaseService externalDatabaseService;

    public DatabaseTaskServiceImpl(AppServiceSchedulerService appServiceSchedulerService, AppServiceObjectService appServiceObjectService, AppServiceTimeProps appServiceTimeProps, DatabaseService databaseService, ExternalDatabaseService externalDatabaseService) {
        super(appServiceSchedulerService, appServiceObjectService, appServiceTimeProps);
        this.databaseService = databaseService;
        this.externalDatabaseService = externalDatabaseService;
    }

    @Override
    protected void onPayoutRejected(AppServiceObject appServiceObject) {
        super.onPayoutRejected(appServiceObject);
        externalDatabaseService.forbidExternalDatabaseConnections(appServiceObject.getId());
    }

    @Override
    protected void onDisabled(AppServiceObject appServiceObject) {
        Integer databaseId = appServiceObject.getId();
        databaseService.deleteDatabaseById(databaseId);
        externalDatabaseService.dropExternalDatabase(databaseId);
    }

}
