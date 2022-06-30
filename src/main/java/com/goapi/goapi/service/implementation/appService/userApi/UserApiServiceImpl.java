package com.goapi.goapi.service.implementation.appService.userApi;

import com.goapi.goapi.domain.model.appService.database.Database;
import com.goapi.goapi.domain.model.appService.tariff.UserApiTariff;
import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.appService.userApi.UserApiNotFoundException;
import com.goapi.goapi.repo.appService.userApi.UserApiRepository;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserApiServiceImpl implements UserApiService {

    private final UserApiRepository userApiRepository;

    @Override
    public UserApi createNewUserApi(User user, UserApiTariff userApiTariff, AppServiceBill userApiAppServiceBill, Database database, String apiName, boolean isProtected) {
        String apiKey = UUID.randomUUID().toString();
        UserApi newUserApi = new UserApi(apiKey, isProtected, database, userApiTariff, apiName, user, userApiAppServiceBill);
        newUserApi = userApiRepository.save(newUserApi);
        return newUserApi;
    }

    @Override
    public UserApi getUserApiByIdWithOwner(Integer apiId) {
        Optional<UserApi> userApi = userApiRepository.findUserApiByIdWithOwner(apiId);
        return userApi.orElseThrow(() -> new UserApiNotFoundException(apiId));
    }

    @Override
    public UserApi getUserApiByIdWithRequestsAndOwner(Integer apiId) {
        Optional<UserApi> userApi = userApiRepository.findUserApiByIdWithOwnerAndRequests(apiId);
        return userApi.orElseThrow(() -> new UserApiNotFoundException(apiId));
    }

    @Override
    public UserApi getUserApiById(Integer apiId) {
        Optional<UserApi> userApi = userApiRepository.findById(apiId);
        return userApi.orElseThrow(() -> new UserApiNotFoundException(apiId));
    }

    @Override
    public void deleteUserApiById(Integer apiId) {
        userApiRepository.deleteById(apiId);
    }

    @Override
    public void refreshUserApiKey(UserApi userApi) {
        String apiKey = UUID.randomUUID().toString();
        userApi.setApiKey(apiKey);
        userApiRepository.save(userApi);
    }

    @Override
    public List<UserApi> getUserApisByUserId(Integer userId) {
        List<UserApi> apis = userApiRepository.findByOwner_id(userId);
        return apis;
    }

    @Override
    public int getUserApiRequestsCountById(Integer userApiId) {
        return userApiRepository.getTotalUserApiRequestsCount(userApiId);
    }

    @Override
    public int getTotalUserApisCount(User user) {
        Integer userId = user.getId();
        return userApiRepository.getTotalUserApisCount(userId);
    }

    @Override
    public List<UserApi> getUserApiOptionalUsingDatabaseWithId(Integer dbId) {
        return userApiRepository.findByDatabase_Id(dbId);
    }

    @Override
    public UserApi getUserApiByIdWithTariffAndOwner(Integer userApiTariffId) {
        Optional<UserApi> userApi = userApiRepository.findUserApiByIdWithTariffAndOwner(userApiTariffId);
        return userApi.orElseThrow(() -> new UserApiNotFoundException(userApiTariffId));
    }

}
