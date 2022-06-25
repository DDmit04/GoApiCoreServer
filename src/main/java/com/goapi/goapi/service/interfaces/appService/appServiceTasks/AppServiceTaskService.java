package com.goapi.goapi.service.interfaces.appService.appServiceTasks;

import com.goapi.goapi.domain.model.appService.AppServiceObject;

public interface AppServiceTaskService {

    void startAppServicePayoutTask(AppServiceObject appServiceObject);
    void startDisabledAppServiceDeletionAwait(AppServiceObject appServiceObject);
}
