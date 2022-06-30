package com.goapi.goapi.service.implementation.appService.appServiceTasks;

import com.goapi.goapi.domain.model.appService.AppServiceObject;
import com.goapi.goapi.domain.model.appService.AppServiceObjectStatus;
import com.goapi.goapi.domain.model.appService.AppServiceStatusType;
import com.goapi.goapi.domain.model.appService.tariff.Tariff;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayout;
import com.goapi.goapi.exception.appService.AppServiceObjectNotFoundException;
import com.goapi.goapi.exception.finances.PaymentRejectedException;
import com.goapi.goapi.service.interfaces.appService.AppServiceObjectService;
import com.goapi.goapi.service.interfaces.appService.appServiceTasks.AppServiceSchedulerService;
import com.goapi.goapi.service.interfaces.finances.payment.AppServicePayoutService;
import com.goapi.goapi.service.interfaces.finances.payment.PaymentProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Consumer;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class AppServiceSchedulerServiceImpl implements AppServiceSchedulerService {

    private final ThreadPoolTaskScheduler appServiceTasksScheduler;
    private final AppServiceObjectService appServiceObjectService;
    private final AppServicePayoutService appServicePayoutService;
    private final PaymentProcessingService paymentProcessingService;

    private Map<Integer, ScheduledFuture<?>> currentPayoutTasks = new HashMap<>();
    private Map<Integer, ScheduledFuture<?>> currentDeleteTasks = new HashMap<>();

    @Lookup
    public AppServiceSchedulerService getThisProxy() {
        return null;
    }

    @Override
    public void interruptAppServicePayoutTask(Integer appServiceId) {
        stopPayoutTask(appServiceId);
    }

    @Override
    public void runAppServicePayoutTask(Integer appServiceObjectId, Date nextPayoutDate, Duration duration,
                                        Consumer<AppServiceObject> onNotEnoughMoney,
                                        Consumer<Integer> onThreadException) {
        try {
            ScheduledFuture<?> t = appServiceTasksScheduler.scheduleAtFixedRate(() ->
                    getThisProxy().doPayout(appServiceObjectId, onNotEnoughMoney),
                nextPayoutDate,
                duration.toMillis());
            currentPayoutTasks.put(appServiceObjectId, t);
        } catch (TaskRejectedException e) {
            onThreadException.accept(appServiceObjectId);
        }
    }

    @Override
    public void runAppServiceDeletionTask(Integer appServiceObjectId, Date execDate, Consumer<AppServiceObject> onAppServiceDisabled, Consumer<Integer> onThreadException) {
        try {
            ScheduledFuture<?> t = appServiceTasksScheduler.schedule(() ->
                    getThisProxy().doDeletion(appServiceObjectId, onAppServiceDisabled),
                execDate);
            currentDeleteTasks.put(appServiceObjectId, t);
        } catch (TaskRejectedException e) {
            onThreadException.accept(appServiceObjectId);
        }
    }

    @Override
    public void doDeletion(Integer appServiceObjectId, Consumer<AppServiceObject> onAppServiceDisabled) {
        try {
            AppServiceObject appServiceObject = appServiceObjectService.getAppServiceObjectById(appServiceObjectId);
            AppServiceObjectStatus appServiceObjectStatus = appServiceObject.getAppServiceObjectStatus();
            AppServiceStatusType appServiceStatusType = appServiceObjectStatus.getStatus();
            if (appServiceStatusType == AppServiceStatusType.DISABLED) {
                onAppServiceDisabled.accept(appServiceObject);
            }
        } catch (AppServiceObjectNotFoundException e) {
            stopDeleteTask(appServiceObjectId);
        }
    }

    @Override
    public void doPayout(Integer appServiceObjectId, Consumer<AppServiceObject> onNotEnoughMoney) {
        AppServiceObject appServiceObject = null;
        try {
            appServiceObject = appServiceObjectService.getAppServiceObjectByIdWithTariffAndBill(appServiceObjectId);
            AppServiceObjectStatus appServiceObjectStatus = appServiceObject.getAppServiceObjectStatus();
            AppServiceStatusType appServiceStatusType = appServiceObjectStatus.getStatus();
            if (appServiceStatusType == AppServiceStatusType.ACTIVE) {
                Tariff userApiTariff = appServiceObject.getAppServiceTariff();
                AppServiceBill appServiceBill = appServiceObject.getAppServiceBill();
                BigDecimal costPerDay = userApiTariff.getCostPerDay();
                AppServicePayout appServicePayout = appServicePayoutService.createAppServicePayout(costPerDay, appServiceBill);
                paymentProcessingService.processAppServicePayout(appServicePayout);
            }
        } catch (AppServiceObjectNotFoundException e) {
            stopPayoutTask(appServiceObjectId);
        } catch (PaymentRejectedException e) {
            onNotEnoughMoney.accept(appServiceObject);
            stopPayoutTask(appServiceObjectId);
        }
    }

    private void stopPayoutTask(Integer id) {
        ScheduledFuture<?> scheduledFuture = currentPayoutTasks.get(id);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            currentPayoutTasks.remove(id);
        }
    }

    private void stopDeleteTask(Integer id) {
        ScheduledFuture<?> scheduledFuture = currentDeleteTasks.get(id);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            currentDeleteTasks.remove(id);
        }
    }

}
