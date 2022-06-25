package com.goapi.goapi.service.implementation.appService.appServiceTasks;

import com.goapi.goapi.domain.model.appService.AppServiceObject;
import com.goapi.goapi.domain.model.finances.bill.BillType;
import com.goapi.goapi.service.interfaces.appService.appServiceTasks.AppServiceSchedulerService;
import com.goapi.goapi.service.interfaces.appService.appServiceTasks.AppServiceTaskService;
import com.goapi.goapi.service.interfaces.appService.appServiceTasks.AppServiceTaskServiceFacade;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
public class AppServiceTaskServiceFacadeImpl implements AppServiceTaskServiceFacade {

    private final AppServiceTaskService userApiTaskService;
    private final AppServiceTaskService databaseTaskService;
    private final AppServiceSchedulerService appServiceSchedulerService;

    public AppServiceTaskServiceFacadeImpl(@Qualifier("UserApiTaskService") AppServiceTaskService userApiTaskService,
                                           @Qualifier("DatabaseTaskService") AppServiceTaskService databaseTaskService,
                                           AppServiceSchedulerService appServiceSchedulerService) {
        this.userApiTaskService = userApiTaskService;
        this.databaseTaskService = databaseTaskService;
        this.appServiceSchedulerService = appServiceSchedulerService;
    }

    @Override
    public void startAppServiceNextPayoutTask(AppServiceObject appServiceObject) {
        AppServiceTaskService targetService = getAppServiceTaskService(appServiceObject);
        targetService.startAppServicePayoutTask(appServiceObject);
    }

    @Override
    public void restartAppServiceNextPayoutTask(AppServiceObject appServiceObject) {
        Integer appServiceId = appServiceObject.getId();
        appServiceSchedulerService.interruptAppServicePayoutTask(appServiceId);
        startAppServiceNextPayoutTask(appServiceObject);
    }

    @Override
    public void startDisabledAppServiceNextDeletionTask(AppServiceObject appServiceObject) {
        AppServiceTaskService targetService = getAppServiceTaskService(appServiceObject);
        targetService.startDisabledAppServiceDeletionAwait(appServiceObject);
    }

    private AppServiceTaskService getAppServiceTaskService(AppServiceObject appServiceObject) {
        BillType billType = appServiceObject.getAppServiceBill().getBillType();
        AppServiceTaskService targetService =
            switch (billType) {
                case DATABASE -> databaseTaskService;
                case USER_API -> userApiTaskService;
            };
        return targetService;
    }

}
