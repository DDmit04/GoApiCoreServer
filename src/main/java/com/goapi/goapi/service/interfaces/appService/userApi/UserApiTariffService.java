package com.goapi.goapi.service.interfaces.appService.userApi;

import com.goapi.goapi.domain.dto.appServiceobject.tariff.UserApiTariffDto;
import com.goapi.goapi.domain.model.appService.tariff.UserApiTariff;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserApiTariffService {
    UserApiTariff getUserApiTariffById(Integer id);

    List<UserApiTariffDto> listUserApiTariffs();

}
