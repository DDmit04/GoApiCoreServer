package com.goapi.goapi.service.interfaces.appService.userApi;

import com.goapi.goapi.domain.model.appService.database.Database;
import com.goapi.goapi.domain.model.appService.tariff.UserApiTariff;
import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.user.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserApiService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    UserApi createNewUserApi(User user, UserApiTariff userApiTariff, AppServiceBill userApiAppServiceBill, Database db, String apiName, boolean isProtected);
    UserApi getUserApiById(Integer apiId);
    UserApi getUserApiByIdWithOwner(Integer apiId);
    UserApi getUserApiByIdWithRequestsAndOwner(Integer apiId);
    UserApi getUserApiByIdWithTariffAndOwner(Integer userApiTariffId);
    List<UserApi> getUserApisByUserId(Integer userId);
    void deleteUserApiById(Integer apiId);
    void refreshUserApiKey(UserApi userApi);
    int getUserApiRequestsCountById(Integer userApiId);
    int getTotalUserApisCount(User user);
    List<UserApi> getUserApiOptionalUsingDatabaseWithId(Integer dbId);
}
