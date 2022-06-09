package com.goapi.goapi.service.interfaces.userApi;

import com.goapi.goapi.domain.dto.tariff.UserApiTariffDto;
import com.goapi.goapi.domain.model.userApi.UserApiTariff;

import java.util.List;

public interface UserApiTariffService {
    UserApiTariff getUserApiTariffById(Integer id);

    List<UserApiTariffDto> listUserApiTariffs();

}
