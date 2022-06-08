package com.goapi.goapi.service.implementation.userApi;

import com.goapi.goapi.domain.model.database.Database;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.userApi.UserApi;
import com.goapi.goapi.domain.model.userApi.UserApiTariff;
import com.goapi.goapi.exception.tariff.userApi.UserApiTariffChosenException;
import com.goapi.goapi.exception.userApi.UserApiNotFoundException;
import com.goapi.goapi.exception.userApi.UserApiOwnerException;
import com.goapi.goapi.repo.userApi.UserApiRepository;
import com.goapi.goapi.service.interfaces.userApi.UserApiService;
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
    public UserApi createUserApi(String apiName, boolean isProtected, Database db, User user) {
        UserApiTariff userApiTariff = user.getUserApiTariff();
        if(userApiTariff == null) {
            Integer userId = user.getId();
            throw new UserApiTariffChosenException(userId);
        }
        String apiKey = UUID.randomUUID().toString();
        UserApi newUserApi = new UserApi(apiKey, isProtected, db, apiName, user);
        newUserApi = userApiRepository.save(newUserApi);
        return newUserApi;
    }

    @Override
    public Optional<UserApi> getUserApiById(Integer apiId) {
        return userApiRepository.findById(apiId);
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
    public UserApi getApiByIdCheckOwner(User user, Integer apiId) {
        Optional<UserApi> userApiOptional = getUserApiById(apiId);
        return userApiOptional.map(userApi -> {
            isApiOwnerOrThrow(user, userApi);
            return userApi;
        }).orElseThrow(() -> new UserApiNotFoundException(apiId));
    }

    @Override
    public List<UserApi> getUserApisByUserId(Integer userId) {
        List<UserApi> apis = userApiRepository.findByOwner_id(userId);
        return apis;
    }

    @Override
    public int getTotalUserApisRequestsCount(Integer userId) {
        return userApiRepository.getTotalUserApiRequestsCount(userId);
    }

    @Override
    public boolean renameUserApi(UserApi userApi, String newUserApiName) {
        userApi.setName(newUserApiName);
        userApiRepository.save(userApi);
        return true;
    }

    private void isApiOwnerOrThrow(User user, UserApi api) {
        if (!api.getOwner().equals(user)) {
            Integer userId = user.getId();
            throw new UserApiOwnerException(userId, api.getId());
        }
    }
}
