package com.goapi.goapi.service.facase;

import com.goapi.goapi.controller.form.api.CreateUserApiRequest;
import com.goapi.goapi.controller.form.api.UserApiShortData;
import com.goapi.goapi.domain.api.UserApiTariff;
import com.goapi.goapi.domain.api.UserApi;
import com.goapi.goapi.domain.api.request.UserApiRequest;
import com.goapi.goapi.domain.database.Database;
import com.goapi.goapi.domain.user.User;
import com.goapi.goapi.service.api.UserApiRequestService;
import com.goapi.goapi.service.api.UserApiTariffService;
import com.goapi.goapi.service.api.UserApiService;
import com.goapi.goapi.service.database.DatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserApiServiceFacade {

    private final UserApiTariffService userApiTariffService;
    private final UserApiService userApiService;
    private final DatabaseService databaseService;
    private final UserApiRequestService userApiRequestService;

    public UserApiShortData createApi(User user, CreateUserApiRequest createUserApiRequest) {
        Integer dbId = createUserApiRequest.getDatabaseId();
        Optional<Database> databaseOptional = databaseService.getDatabaseById(dbId);
        return databaseOptional.map(database -> {
            Integer apiTariffId = createUserApiRequest.getApiTariffId();
            boolean isProtected = createUserApiRequest.isProtected();
            String apiName = createUserApiRequest.getName();
            Optional<UserApiTariff> apiTariffOptional = userApiTariffService.getDatabaseTariffById(apiTariffId);
            return apiTariffOptional
                .map(apiTariff -> userApiService.createUserApi(apiName, isProtected, apiTariff, database, user))
                .orElseThrow(() -> new RuntimeException());
        }).orElseThrow(() -> new RuntimeException());

    }

    public boolean deleteApi(User user, Integer apiId) {
        UserApi userApi = userApiService.getApiByIdCheckOwner(apiId, user);
        if(userApi != null) {
            userApiService.deleteUserApiById(apiId);
            return true;
        }
        return false;
    }

    public String getUserApiKey(User user, Integer apiId) {
        UserApi userApi = userApiService.getApiByIdCheckOwner(apiId, user);
        if(userApi != null) {
            userApiService.deleteUserApiById(apiId);
            return userApi.getApiKey();
        }
        throw new RuntimeException();
    }

    public boolean refreshApiKey(User user, Integer apiId) {
        UserApi userApi = userApiService.getApiByIdCheckOwner(apiId, user);
        if(userApi != null) {
            userApiService.refreshUserApiKey(userApi);
            return true;
        }
        throw new RuntimeException();
    }

}
