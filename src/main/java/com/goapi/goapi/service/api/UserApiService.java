package com.goapi.goapi.service.api;

import com.goapi.goapi.controller.form.api.UserApiShortData;
import com.goapi.goapi.domain.api.UserApiTariff;
import com.goapi.goapi.domain.api.UserApi;
import com.goapi.goapi.domain.database.Database;
import com.goapi.goapi.domain.user.User;
import com.goapi.goapi.repo.api.UserApiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserApiService {

    private final UserApiRepository userApiRepository;
    public UserApiShortData createUserApi(String apiName, boolean isProtected, UserApiTariff userApiTariff, Database db, User user) {
        String apiKey = UUID.randomUUID().toString();
        UserApi newUserApi = new UserApi(apiKey, isProtected, userApiTariff, db, apiName, user);
        newUserApi = userApiRepository.save(newUserApi);
        UserApiShortData newUSerApiData = new UserApiShortData(newUserApi.getId(), newUserApi.getName(), newUserApi.isProtected());
        return newUSerApiData;
    }

    public Optional<UserApi> findApiById(Integer apiId) {
        return userApiRepository.findById(apiId);
    }

    public void deleteUserApiById(Integer apiId) {
        userApiRepository.deleteById(apiId);
    }

    public void refreshUserApiKey(UserApi userApi) {
        String apiKey = UUID.randomUUID().toString();
        userApi.setApiKey(apiKey);
        userApiRepository.save(userApi);
    }

    public UserApi getApiByIdCheckOwner(Integer apiId, User user) {
        Optional<UserApi> userApiOptional = findApiById(apiId);
        return userApiOptional.map(userApi -> {
            isApiOwnerOrThrow(userApi, user);
            return userApi;
        }).orElseThrow(() -> new RuntimeException());
    }

    public void isApiOwnerOrThrow(UserApi api, User user) {
        if (!api.getOwner().equals(user)) {
            throw new RuntimeException();
        }
    }

}
