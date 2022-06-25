package com.goapi.goapi.service.implementation.appService.appServiceTasks;

import com.goapi.goapi.props.appServiceTimeProps.AppServiceTimeProps;
import com.goapi.goapi.service.interfaces.appService.AppServiceObjectService;
import com.goapi.goapi.service.interfaces.appService.appServiceTasks.AppServiceSchedulerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@Qualifier("UserApiTaskService")
public class UserApiTaskServiceImpl extends CommonApiTaskService {

    public UserApiTaskServiceImpl(AppServiceSchedulerService appServiceSchedulerService, AppServiceObjectService appServiceObjectService, AppServiceTimeProps appServiceTimeProps) {
        super(appServiceSchedulerService, appServiceObjectService, appServiceTimeProps);
    }
}
