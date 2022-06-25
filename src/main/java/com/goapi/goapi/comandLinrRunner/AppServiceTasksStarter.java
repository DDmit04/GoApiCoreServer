package com.goapi.goapi.comandLinrRunner;

import com.goapi.goapi.domain.model.appService.AppServiceObject;
import com.goapi.goapi.domain.model.appService.AppServiceStatusType;
import com.goapi.goapi.service.interfaces.appService.AppServiceObjectService;
import com.goapi.goapi.service.interfaces.appService.appServiceTasks.AppServiceTaskServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppServiceTasksStarter implements CommandLineRunner {

    private final AppServiceObjectService appServiceObjectService;
    private final AppServiceTaskServiceFacade appServiceTaskServiceFacade;


    @Override
    public void run(String... args) {
        List<AppServiceObject> allAppServiceObjects = appServiceObjectService.getAllAppServiceObjects();
        allAppServiceObjects
            .forEach(appServiceObject -> {
                AppServiceStatusType appServiceObjectStatus = appServiceObject.getAppServiceObjectStatus().getStatus();
                if (appServiceObjectStatus == AppServiceStatusType.DISABLED) {
                    appServiceTaskServiceFacade.startDisabledAppServiceNextDeletionTask(appServiceObject);
                } else if (appServiceObjectStatus == AppServiceStatusType.ACTIVE) {
                    appServiceTaskServiceFacade.startAppServiceNextPayoutTask(appServiceObject);
                }
            });
    }
}