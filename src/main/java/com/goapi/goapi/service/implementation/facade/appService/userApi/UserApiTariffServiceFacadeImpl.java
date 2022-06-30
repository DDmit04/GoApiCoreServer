package com.goapi.goapi.service.implementation.facade.appService.userApi;

import com.goapi.goapi.domain.dto.appServiceobject.tariff.UserApiTariffDto;
import com.goapi.goapi.domain.model.appService.tariff.UserApiTariff;
import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.tariff.userApi.UserApiTariffConditionChangeException;
import com.goapi.goapi.service.interfaces.appService.AppServiceObjectService;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiService;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiTariffService;
import com.goapi.goapi.service.interfaces.facade.finances.PaymentsServiceFacade;
import com.goapi.goapi.service.interfaces.facade.userApi.UserApiTariffServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserApiTariffServiceFacadeImpl implements UserApiTariffServiceFacade {

    private final UserApiTariffService userApiTariffService;
    private final AppServiceObjectService appServiceObjectService;
    private final UserApiService userApiService;
    private final PaymentsServiceFacade paymentsServiceFacade;

    @Override
    public UserApiTariffDto getUserApiTariff(User user, Integer apiId) {
        UserApi userApi = getUserApiWithTariffCheckOwner(user, apiId);
        UserApiTariff userApiTariff = userApi.getAppServiceTariff();
        UserApiTariffDto userApiTariffDto = new UserApiTariffDto(
            userApiTariff.getId(),
            userApiTariff.getTariffName(),
            userApiTariff.getCostPerDay(),
            userApiTariff.getMaxRequestsCount());
        return userApiTariffDto;
    }

    @Override
    public void changeUserApiTariff(User user, Integer userApiTariffId, Integer tariffId) {
        UserApiTariff newTariff = userApiTariffService.getUserApiTariffById(tariffId);
        UserApi userApi = getUserApiWithTariffCheckOwner(user, userApiTariffId);
        changeUserApiTariff(userApi, newTariff);
        appServiceObjectService.disableAppServiceObject(userApi);
        paymentsServiceFacade.makeFirstAppServicePaymentAfterTariffChange(user, userApi);
    }

    private UserApi getUserApiWithTariffCheckOwner(User user, Integer userApiTariffId) {
        UserApi userApi = userApiService.getUserApiByIdWithTariffAndOwner(userApiTariffId);
        appServiceObjectService.isAppServiceObjectOwnerOrThrow(user, userApi);
        return userApi;
    }

    private void changeUserApiTariff(UserApi userApi, UserApiTariff newTariff) {
        UserApiTariff currentUserApiTariff = userApi.getAppServiceTariff();
        if (!currentUserApiTariff.equals(newTariff)) {
            int newMaxRequestsCount = newTariff.getMaxRequestsCount();
            Integer userApiId = userApi.getId();
            int existingRequestsCount = userApiService.getUserApiRequestsCountById(userApiId);
            boolean canChange = existingRequestsCount <= newMaxRequestsCount;
            if (canChange) {
                appServiceObjectService.changeAppServiceObjectTariff(userApi, newTariff);
            } else {
                Integer currentTariffId = currentUserApiTariff.getId();
                Integer newTariffId = newTariff.getId();
                throw new UserApiTariffConditionChangeException(newTariffId, currentTariffId);
            }
        }
    }
}
