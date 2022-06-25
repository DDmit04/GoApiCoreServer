package com.goapi.goapi.service.implementation.appService;

import com.goapi.goapi.domain.model.appService.AppServiceObject;
import com.goapi.goapi.domain.model.appService.AppServiceObjectStatus;
import com.goapi.goapi.domain.model.appService.AppServiceStatusType;
import com.goapi.goapi.domain.model.appService.tariff.Tariff;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.appService.AppServiceObjectNotFoundException;
import com.goapi.goapi.exception.appService.AppServiceObjectOwnerException;
import com.goapi.goapi.repo.appService.AppServiceObjectRepository;
import com.goapi.goapi.service.interfaces.appService.AppServiceObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class AppServiceObjectServiceImpl implements AppServiceObjectService {

    private final AppServiceObjectRepository appServiceObjectRepository;

    @Override
    public void disableAppServiceObject(AppServiceObject appServiceObject) {
        setAppServiceObjectDisabled(appServiceObject, AppServiceStatusType.DISABLED);
    }

    @Override
    public void enableAppServiceObject(AppServiceObject appServiceObject) {
        setAppServiceObjectDisabled(appServiceObject, AppServiceStatusType.ACTIVE);
    }

    @Override
    public boolean isAppServiceObjectOwnerOrThrow(User user, AppServiceObject appServiceObject) {
        boolean isOwner = appServiceObject.getOwner().equals(user);
        if (isOwner) {
            return true;
        }
        throw new AppServiceObjectOwnerException(user.getId(), appServiceObject.getId());
    }

    @Override
    public void renameAppServiceObjectApi(AppServiceObject appServiceObject, String newName) {
        appServiceObject.setName(newName);
        appServiceObjectRepository.save(appServiceObject);
    }

    @Override
    public AppServiceObject getAppServiceObjectByIdWithTariffAndBill(Integer appServiceId) {
        Optional<AppServiceObject> appServiceObjectOptional = appServiceObjectRepository.findAppServiceObjectByIdWithTariffAndBill(appServiceId);
        return appServiceObjectOptional.orElseThrow(() -> new AppServiceObjectNotFoundException(appServiceId));
    }

    @Override
    public List<AppServiceObject> getAllAppServiceObjects() {
        return appServiceObjectRepository.findAll();
    }

    @Override
    public AppServiceObject getAppServiceObjectById(Integer appServiceId) {
        Optional<AppServiceObject> appServiceObjectOptional = appServiceObjectRepository.findAppServiceObjectById(appServiceId);
        return appServiceObjectOptional.orElseThrow(() -> new AppServiceObjectNotFoundException(appServiceId));
    }

    @Override
    public void deleteAppServiceObjectById(Integer appServiceObjectId) {
        appServiceObjectRepository.deleteById(appServiceObjectId);
    }

    @Override
    public void changeAppServiceObjectTariff(AppServiceObject appServiceObject, Tariff newTariff) {
        appServiceObject.setAppServiceTariff(newTariff);
        appServiceObjectRepository.save(appServiceObject);
    }

    @Override
    public AppServiceObject getAppServiceObjectByIdWithOwner(Integer appServiceId) {
        Optional<AppServiceObject> appServiceObjectOptional = appServiceObjectRepository.findAppServiceObjectByIdWithOwner(appServiceId);
        return appServiceObjectOptional.orElseThrow(() -> new AppServiceObjectNotFoundException(appServiceId));
    }

    private void setAppServiceObjectDisabled(AppServiceObject appServiceObject, AppServiceStatusType appServiceStatusType) {
        AppServiceObjectStatus appServiceObjectStatus = appServiceObject.getAppServiceObjectStatus();
        appServiceObjectStatus.setStatus(AppServiceStatusType.DISABLED);
        appServiceObjectStatus.setStatusDate(Timestamp.valueOf(LocalDateTime.now()));
        appServiceObjectRepository.save(appServiceObject);
    }
}
