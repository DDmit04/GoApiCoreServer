package com.goapi.goapi.service.interfaces.appService.userApi;

import com.goapi.goapi.domain.model.appService.database.Database;
import com.goapi.goapi.domain.model.appService.tariff.UserApiTariff;
import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.user.User;

import java.util.List;

public interface UserApiService {
    UserApi createUserApi(String apiName, boolean isProtected, UserApiTariff userApiTariff, Database db, User user, AppServiceBill userApiAppServiceBill);

    void deleteUserApiById(Integer apiId);

    void refreshUserApiKey(UserApi userApi);

    List<UserApi> getUserApisByUserId(Integer userId);

    int getUserApiRequestsCountById(Integer userApiId);

    boolean renameUserApi(UserApi userApi, String newUserApiName);

    void isApiOwnerOrThrow(User user, UserApi api);

    UserApi getUserApiByIdWithTariffAndOwner(Integer userApiTariffId);

    void setUserApiTariff(UserApi userApi, UserApiTariff newTariff);

    UserApi getUserApiByIdWithOwner(Integer apiId);

    UserApi getUserApiByIdWithRequestsAndOwner(Integer apiId);

    UserApi getUserApiById(Integer apiId);
}
