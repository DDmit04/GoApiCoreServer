package com.goapi.goapi.service.interfaces.appService;

import com.goapi.goapi.domain.model.appService.AppServiceObject;
import com.goapi.goapi.domain.model.appService.tariff.Tariff;
import com.goapi.goapi.domain.model.user.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface AppServiceObjectService {

    List<AppServiceObject> getAllAppServiceObjects();
    AppServiceObject getAppServiceObjectById(Integer appServiceId);
    AppServiceObject getAppServiceObjectByIdWithTariffAndBill(Integer appServiceId);
    AppServiceObject getAppServiceObjectByIdWithOwner(Integer apiId);
    void disableAppServiceObject(AppServiceObject appServiceObject);
    void enableAppServiceObject(AppServiceObject appServiceObject);
    boolean isAppServiceObjectOwnerOrThrow(User user, AppServiceObject appServiceObject);
    void renameAppServiceObjectApi(AppServiceObject appServiceObject, String newName);
    void deleteAppServiceObjectById(Integer appServiceObjectId);
    void changeAppServiceObjectTariff(AppServiceObject appServiceObject, Tariff newTariff);
}
