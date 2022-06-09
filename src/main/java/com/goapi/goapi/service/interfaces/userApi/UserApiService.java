package com.goapi.goapi.service.interfaces.userApi;

import com.goapi.goapi.domain.model.bill.AppServiceBill;
import com.goapi.goapi.domain.model.database.Database;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.userApi.UserApi;

import java.util.List;

public interface UserApiService {
    UserApi createUserApi(String apiName, boolean isProtected, Database db, User user, AppServiceBill userApiAppServiceBill);

    UserApi getUserApiById(Integer apiId);

    void deleteUserApiById(Integer apiId);

    void refreshUserApiKey(UserApi userApi);

    List<UserApi> getUserApisByUserId(Integer userId);

    int getTotalUserApisRequestsCount(Integer userId);

    boolean renameUserApi(UserApi userApi, String newUserApiName);

    void isApiOwnerOrThrow(User user, UserApi api);
}
