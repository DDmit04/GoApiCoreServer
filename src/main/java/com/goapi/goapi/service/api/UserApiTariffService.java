package com.goapi.goapi.service.api;

import com.goapi.goapi.domain.api.UserApiTariff;
import com.goapi.goapi.repo.api.ApiTariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserApiTariffService {

    private final ApiTariffRepository apiTariffRepository;

    public Optional<UserApiTariff> getDatabaseTariffById(Integer id) {
        return apiTariffRepository.findById(id);
    }

    public List<UserApiTariff> listTariffs() {
        return apiTariffRepository.findAll();
    }
}
