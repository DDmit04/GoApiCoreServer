package com.goapi.goapi.service.interfaces.appService.appServiceTasks;

import com.goapi.goapi.domain.model.appService.AppServiceObject;

import java.time.Duration;
import java.util.Date;
import java.util.function.Consumer;

public interface AppServiceSchedulerService {

    void interruptAppServicePayoutTask(Integer appServiceId);
    void runAppServicePayoutTask(Integer appServiceObjectId, Date lastPayoutDate, Duration duration,
                                 Consumer<AppServiceObject> onNotEnoughMoney,
                                 Consumer<Integer> onThreadException);
    void runAppServiceDeletionTask(Integer appServiceObjectId, Date execDate,
                                   Consumer<AppServiceObject> onAppServiceDisabled,
                                   Consumer<Integer> onThreadException);
}
