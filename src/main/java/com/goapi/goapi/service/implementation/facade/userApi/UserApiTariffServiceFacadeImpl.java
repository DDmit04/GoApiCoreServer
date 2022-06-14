package com.goapi.goapi.service.implementation.facade.userApi;

import com.goapi.goapi.domain.dto.tariff.UserApiTariffDto;
import com.goapi.goapi.domain.model.appService.tariff.UserApiTariff;
import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.tariff.userApi.UserApiTariffConditionChangeException;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiService;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiTariffService;
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
    private final UserApiService userApiService;

    @Override
    public UserApiTariffDto getUserApiTariff(User user, Integer apiId) {
        UserApi userApi = getUserApiWithTariffCheckOwner(user, apiId);
        UserApiTariff userApiTariff = userApi.getUserApiTariff();
        UserApiTariffDto userApiTariffDto = new UserApiTariffDto(
            userApiTariff.getId(),
            userApiTariff.getTariff_name(),
            userApiTariff.getCostPerMonth(),
            userApiTariff.getMaxRequestsCount());
        return userApiTariffDto;
    }

    @Override
    public boolean changeUserApiTariff(User user, Integer userApiTariffId, Integer tariffId) {
        UserApiTariff newTariff = userApiTariffService.getUserApiTariffById(tariffId);
        UserApi userApi = getUserApiWithTariffCheckOwner(user, userApiTariffId);
        boolean changed = changeUserApiTariff(userApi, newTariff);
        return changed;
    }

    private UserApi getUserApiWithTariffCheckOwner(User user, Integer userApiTariffId) {
        UserApi userApi = userApiService.getUserApiByIdWithTariffAndOwner(userApiTariffId);
        userApiService.isApiOwnerOrThrow(user, userApi);
        return userApi;
    }

    private boolean changeUserApiTariff(UserApi userApi, UserApiTariff newTariff) {
        UserApiTariff currentUserApiTariff = userApi.getUserApiTariff();
        if (!currentUserApiTariff.equals(newTariff)) {
            int newMaxRequestsCount = newTariff.getMaxRequestsCount();
            Integer userApiId = userApi.getId();
            int existingRequestsCount = userApiService.getUserApiRequestsCountById(userApiId);
            boolean canChange = existingRequestsCount <= newMaxRequestsCount;
            if (canChange) {
                userApiService.setUserApiTariff(userApi, newTariff);
                return true;
            } else {
                Integer currentTariffId = currentUserApiTariff.getId();
                Integer newTariffId = newTariff.getId();
                throw new UserApiTariffConditionChangeException(newTariffId, currentTariffId);
            }
        }
        return false;
    }
}
