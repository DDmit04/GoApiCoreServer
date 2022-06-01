package com.goapi.goapi.service.implementation.facase;

import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.userApi.UserApiTariff;
import com.goapi.goapi.exception.tariff.userApi.UserApiTariffNotFoundException;
import com.goapi.goapi.service.interfaces.UserService;
import com.goapi.goapi.service.interfaces.facase.UserApiTariffServiceFacade;
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

    @Override
    public boolean changeUserApiTariff(User user, Integer tariffId) {
        Optional<UserApiTariff> tariffOptional = userApiTariffService.getUserApiTariffById(tariffId);
        return tariffOptional
            .map(tariff -> userService.changeUserApiTariff(user, tariff))
            .orElseThrow(() -> new UserApiTariffNotFoundException(tariffId));
    }

    @Override
    public void setUserApiTariff(User user, Integer tariffId) {
        Optional<UserApiTariff> tariffOptional = userApiTariffService.getUserApiTariffById(tariffId);
        if (tariffOptional.isPresent()) {
            UserApiTariff tariff = tariffOptional.get();
            userService.changeUserApiTariff(user, tariff);
        } else {
            throw new UserApiTariffNotFoundException(tariffId);
        }
    }
}
