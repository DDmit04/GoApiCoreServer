package com.goapi.goapi.service.interfaces.appService.userApi;

import com.goapi.goapi.domain.dto.tariff.UserApiTariffDto;
import com.goapi.goapi.domain.model.appService.tariff.UserApiTariff;

import java.util.List;

public interface UserApiTariffService {
    UserApiTariff getUserApiTariffById(Integer id);

    List<UserApiTariffDto> listUserApiTariffs();

}
