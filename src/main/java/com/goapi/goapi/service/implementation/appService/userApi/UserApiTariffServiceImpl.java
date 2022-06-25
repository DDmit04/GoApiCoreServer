package com.goapi.goapi.service.implementation.appService.userApi;

import com.goapi.goapi.domain.dto.appServiceobject.tariff.UserApiTariffDto;
import com.goapi.goapi.domain.model.appService.tariff.UserApiTariff;
import com.goapi.goapi.exception.tariff.userApi.UserApiTariffNotFoundException;
import com.goapi.goapi.repo.appService.userApi.ApiTariffRepository;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiTariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserApiTariffServiceImpl implements UserApiTariffService {

    private final ApiTariffRepository apiTariffRepository;

    @Override
    public UserApiTariff getUserApiTariffById(Integer tariffId) {
        Optional<UserApiTariff> tariffOptional = apiTariffRepository.findById(tariffId);
        return tariffOptional.orElseThrow(() -> new UserApiTariffNotFoundException(tariffId));
    }

    @Override
    public List<UserApiTariffDto> listUserApiTariffs() {
        List<UserApiTariff> userApiTariffs = apiTariffRepository.findAll();
        List<UserApiTariffDto> userApiTariffDtoList = userApiTariffs.stream()
            .map(tariff -> new UserApiTariffDto(
                tariff.getId(),
                tariff.getTariff_name(),
                tariff.getCostPerDay(),
                tariff.getMaxRequestsCount())
            )
            .collect(Collectors.toList());
        return userApiTariffDtoList;
    }

}
