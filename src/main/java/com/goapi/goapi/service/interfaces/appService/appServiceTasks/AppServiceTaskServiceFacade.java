package com.goapi.goapi.service.interfaces.appService.appServiceTasks;

import com.goapi.goapi.domain.model.appService.AppServiceObject;

public interface AppServiceTaskServiceFacade {

    void startAppServiceNextPayoutTask(AppServiceObject appServiceObject);
    void restartAppServiceNextPayoutTask(AppServiceObject appServiceObject);
    void startDisabledAppServiceNextDeletionTask(AppServiceObject appServiceObject);
}
