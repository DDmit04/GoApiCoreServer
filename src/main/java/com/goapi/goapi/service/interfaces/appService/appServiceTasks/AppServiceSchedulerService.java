package com.goapi.goapi.service.interfaces.appService.appServiceTasks;

import com.goapi.goapi.domain.model.appService.AppServiceObject;
import com.goapi.goapi.exception.finances.PaymentRejectedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.function.Consumer;

public interface AppServiceSchedulerService {

    void interruptAppServicePayoutTask(Integer appServiceId);
    void runAppServicePayoutTask(Integer appServiceObjectId, Date nextPayoutDate, Duration duration,
                                 Consumer<AppServiceObject> onNotEnoughMoney,
                                 Consumer<Integer> onThreadException);
    void runAppServiceDeletionTask(Integer appServiceObjectId, Date execDate,
                                   Consumer<AppServiceObject> onAppServiceDisabled,
                                   Consumer<Integer> onThreadException);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void doDeletion(Integer appServiceObjectId, Consumer<AppServiceObject> onAppServiceDisabled);

    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = PaymentRejectedException.class)
    void doPayout(Integer appServiceObjectId, Consumer<AppServiceObject> onNotEnoughMoney);
}
