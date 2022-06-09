package com.goapi.goapi.service.implementation.facase.userApi;

import com.goapi.goapi.domain.dto.tariff.UserApiTariffDto;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.userApi.UserApiTariff;
import com.goapi.goapi.exception.tariff.userApi.UserApiTariffConditionChangeException;
import com.goapi.goapi.service.interfaces.facase.userApi.UserApiTariffServiceFacade;
import com.goapi.goapi.service.interfaces.user.UserService;
import com.goapi.goapi.service.interfaces.userApi.UserApiService;
import com.goapi.goapi.service.interfaces.userApi.UserApiTariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserApiTariffServiceFacadeImpl implements UserApiTariffServiceFacade {

    private final UserApiTariffService userApiTariffService;
    private final UserService userService;
    private final UserApiService userApiService;

    @Override
    public UserApiTariffDto getUserApiTariff(User user) {
        UserApiTariff userApiTariff = user.getUserApiTariff();
        UserApiTariffDto userApiTariffDto = new UserApiTariffDto(
            userApiTariff.getId(),
            userApiTariff.getTariff_name(),
            userApiTariff.getCostPerMonth(),
            userApiTariff.getMaxRequestsCount());
        return userApiTariffDto;
    }

    @Override
    public boolean changeUserApiTariff(User user, Integer tariffId) {
        UserApiTariff tariff = userApiTariffService.getUserApiTariffById(tariffId);
        boolean changed = changeUserApiTariff(user, tariff);
        return changed;
    }

    @Override
    public void setUserApiTariff(User user, Integer tariffId) {
        UserApiTariff tariff = userApiTariffService.getUserApiTariffById(tariffId);
        changeUserApiTariff(user, tariff);
    }

    private boolean changeUserApiTariff(User user, UserApiTariff newTariff) {
        UserApiTariff currentUserApiTariff = user.getUserApiTariff();
        if (currentUserApiTariff == null) {
            userService.saveUserApiTariff(user, newTariff);
            return true;
        } else {
            if (!currentUserApiTariff.equals(newTariff)) {
                int newMaxRequestsCount = newTariff.getMaxRequestsCount();
                Integer userId = user.getId();
                int existingRequestsCount = userApiService.getTotalUserApisRequestsCount(userId);
                boolean canChange = existingRequestsCount <= newMaxRequestsCount;
                if (canChange) {
                    userService.saveUserApiTariff(user, newTariff);
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
}
