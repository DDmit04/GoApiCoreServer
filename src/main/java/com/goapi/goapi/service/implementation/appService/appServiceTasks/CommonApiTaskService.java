package com.goapi.goapi.service.implementation.appService.appServiceTasks;

import com.goapi.goapi.domain.model.appService.AppServiceObject;
import com.goapi.goapi.domain.model.appService.AppServiceObjectStatus;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.props.appServiceTimeProps.AppServiceTimeProps;
import com.goapi.goapi.service.interfaces.appService.AppServiceObjectService;
import com.goapi.goapi.service.interfaces.appService.appServiceTasks.AppServiceSchedulerService;
import com.goapi.goapi.service.interfaces.appService.appServiceTasks.AppServiceTaskService;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Daniil Dmitrochenkov
 **/
@RequiredArgsConstructor
public class CommonApiTaskService implements AppServiceTaskService {

    protected final AppServiceSchedulerService appServiceSchedulerService;
    protected final AppServiceObjectService appServiceObjectService;
    protected final AppServiceTimeProps appServiceTimeProps;

    @Override
    public void startAppServicePayoutTask(AppServiceObject appServiceObject) {
        Integer apiId = appServiceObject.getId();
        AppServiceBill appServiceBill = appServiceObject.getAppServiceBill();
        Date lastPayoutDate = appServiceBill.getLastPayoutDate();
        Date nextPayoutDate = addDurationToDate(lastPayoutDate, appServiceTimeProps.getPayoutPeriod());
        Duration payoutPeriod = appServiceTimeProps.getPayoutPeriod();
        appServiceSchedulerService.runAppServicePayoutTask(apiId, nextPayoutDate, payoutPeriod, this::onPayoutRejected, this::onThreadException);
    }

    @Override
    public void startDisabledAppServiceDeletionAwait(AppServiceObject appServiceObject) {
        Integer apiId = appServiceObject.getId();
        AppServiceObjectStatus appServiceObjectStatus = appServiceObject.getAppServiceObjectStatus();
        Date statusDate = appServiceObjectStatus.getStatusDate();
        Duration disabledDeletePeriod = appServiceTimeProps.getDeleteDisabledPeriod();
        Date execDate = addDurationToDate(statusDate, disabledDeletePeriod);
        appServiceSchedulerService.runAppServiceDeletionTask(apiId, execDate, this::onDisabled, this::onThreadException);
    }

    protected void onPayoutRejected(AppServiceObject appServiceObject) {
        appServiceObjectService.disableAppServiceObject(appServiceObject);
        startDisabledAppServiceDeletionAwait(appServiceObject);
    }

    protected void onDisabled(AppServiceObject appServiceObject) {
        Integer appServiceObjectId = appServiceObject.getId();
        appServiceObjectService.deleteAppServiceObjectById(appServiceObjectId);
    }

    protected void onThreadException(Integer appServiceId) {
        AppServiceObject appServiceObject = appServiceObjectService.getAppServiceObjectById(appServiceId);
        appServiceObjectService.disableAppServiceObject(appServiceObject);
    }

    private Date addDurationToDate(Date lastPayoutDate, Duration appServiceTimeProps) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastPayoutDate);
        calendar.setTimeInMillis(lastPayoutDate.getTime() + appServiceTimeProps.toMillis());
        Date nextPayoutDate = calendar.getTime();
        return nextPayoutDate;
    }

}
