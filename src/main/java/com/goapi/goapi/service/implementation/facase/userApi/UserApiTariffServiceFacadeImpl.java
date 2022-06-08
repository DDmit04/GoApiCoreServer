package com.goapi.goapi.service.implementation.facase.userApi;

import com.goapi.goapi.domain.dto.tariff.UserApiTariffDto;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.userApi.UserApiTariff;
import com.goapi.goapi.exception.tariff.userApi.UserApiTariffConditionChangeException;
import com.goapi.goapi.exception.tariff.userApi.UserApiTariffNotFoundException;
import com.goapi.goapi.service.interfaces.facase.userApi.UserApiTariffServiceFacade;
import com.goapi.goapi.service.interfaces.user.UserService;
import com.goapi.goapi.service.interfaces.userApi.UserApiService;
import com.goapi.goapi.service.interfaces.userApi.UserApiTariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        Optional<UserApiTariff> tariffOptional = userApiTariffService.getUserApiTariffById(tariffId);
        return tariffOptional
            .map(tariff -> changeUserApiTariff(user, tariff))
            .orElseThrow(() -> new UserApiTariffNotFoundException(tariffId));
    }

    @Override
    public void setUserApiTariff(User user, Integer tariffId) {
        Optional<UserApiTariff> tariffOptional = userApiTariffService.getUserApiTariffById(tariffId);
        if (tariffOptional.isPresent()) {
            UserApiTariff tariff = tariffOptional.get();
            changeUserApiTariff(user, tariff);
        } else {
            throw new UserApiTariffNotFoundException(tariffId);
        }
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
