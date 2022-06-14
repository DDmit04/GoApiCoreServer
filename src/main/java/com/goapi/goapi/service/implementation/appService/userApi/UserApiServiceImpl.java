package com.goapi.goapi.service.implementation.appService.userApi;

import com.goapi.goapi.domain.model.appService.database.Database;
import com.goapi.goapi.domain.model.appService.tariff.UserApiTariff;
import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.userApi.UserApiNotFoundException;
import com.goapi.goapi.exception.userApi.UserApiOwnerException;
import com.goapi.goapi.repo.userApi.UserApiRepository;
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
    public UserApi createUserApi(String apiName, boolean isProtected, UserApiTariff userApiTariff, Database db, User user, AppServiceBill userApiAppServiceBill) {
        String apiKey = UUID.randomUUID().toString();
        UserApi newUserApi = new UserApi(apiKey, isProtected, db, userApiTariff, apiName, user, userApiAppServiceBill);
        newUserApi = userApiRepository.save(newUserApi);
        return newUserApi;
    }

    @Override
    public UserApi getUserApiByIdWithOwner(Integer apiId) {
        Optional<UserApi> userApi = userApiRepository.findUserApiByIdWithOwner(apiId);
        return userApi.orElseThrow(() -> new UserApiNotFoundException(apiId));
    }

    @Override
    public UserApi getUserApiByIdWithTariffAndOwner(Integer apiId) {
        Optional<UserApi> userApi = userApiRepository.findUserApiByIdWithOwnerAndTariff(apiId);
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
    public boolean renameUserApi(UserApi userApi, String newUserApiName) {
        userApi.setUserApiName(newUserApiName);
        userApiRepository.save(userApi);
        return true;
    }

    @Override
    public void isApiOwnerOrThrow(User user, UserApi api) {
        if (!api.getOwner().equals(user)) {
            Integer userId = user.getId();
            throw new UserApiOwnerException(userId, api.getId());
        }
    }

    @Override
    public void setUserApiTariff(UserApi userApi, UserApiTariff newTariff) {
        userApi.setUserApiTariff(newTariff);
        userApiRepository.save(userApi);
    }
}
