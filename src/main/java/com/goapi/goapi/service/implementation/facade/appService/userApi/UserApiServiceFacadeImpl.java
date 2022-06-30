package com.goapi.goapi.service.implementation.facade.appService.userApi;

import com.goapi.goapi.controller.forms.userApi.CreateUserApiRequest;
import com.goapi.goapi.domain.dto.appServiceobject.AppServiceObjectStatusDto;
import com.goapi.goapi.domain.dto.appServiceobject.userApi.SummaryUserApiDto;
import com.goapi.goapi.domain.dto.appServiceobject.userApi.UserApiDto;
import com.goapi.goapi.domain.dto.appServiceobject.userApi.UserApiRequestDto;
import com.goapi.goapi.domain.model.appService.AppServiceObjectStatus;
import com.goapi.goapi.domain.model.appService.database.Database;
import com.goapi.goapi.domain.model.appService.tariff.UserApiTariff;
import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.appService.userApi.UserApisCountCupException;
import com.goapi.goapi.service.interfaces.appService.AppServiceObjectService;
import com.goapi.goapi.service.interfaces.appService.database.DatabaseService;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiService;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiTariffService;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiUtilsService;
import com.goapi.goapi.service.interfaces.facade.finances.PaymentsServiceFacade;
import com.goapi.goapi.service.interfaces.facade.userApi.UserApiServiceFacade;
import com.goapi.goapi.service.interfaces.finances.bill.AppServiceBillService;
import com.goapi.goapi.service.interfaces.finances.bill.UserBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserApiServiceFacadeImpl implements UserApiServiceFacade {

    private final UserApiService userApiService;
    private final PaymentsServiceFacade paymentsServiceFacade;
    private final AppServiceObjectService appServiceObjectService;
    private final UserApiTariffService userApiTariffService;
    private final DatabaseService databaseService;
    private final UserApiUtilsService userApiUtilsService;
    private final AppServiceBillService appServiceBillService;
    private final UserBillService userBillService;

    @Value("${limit.max-apis-count}")
    private int userMaxApisCount;

    @Override
    public SummaryUserApiDto createNewUserApi(User user, CreateUserApiRequest createUserApiRequest) {
        int currentUserApisCount = userApiService.getTotalUserApisCount(user);
        if(currentUserApisCount <= userMaxApisCount) {
            Integer dbId = createUserApiRequest.getDatabaseId();
            Integer tariffId = createUserApiRequest.getTariffId();
            UserApiTariff userApiTariff = userApiTariffService.getUserApiTariffById(tariffId);
            boolean isProtected = createUserApiRequest.isProtected();
            String userApiName = createUserApiRequest.getName();
            Database database = databaseService.getDatabaseById(dbId);
            AppServiceBill userApiAppServiceBill = appServiceBillService.createUserApiBill();
            UserApi userApi = userApiService.createNewUserApi(user, userApiTariff, userApiAppServiceBill, database, userApiName, isProtected);
            paymentsServiceFacade.makeFirstAppServicePayment(user, userApi);
            SummaryUserApiDto apiDto = getSummaryUserApiDto(userApi);
            return apiDto;
        }
        Integer userId = user.getId();
        throw new UserApisCountCupException(userId);
    }

    @Override
    public boolean deleteApi(User user, Integer apiId) {
        UserApi userApi = getUserApiByIdCheckOwner(user, apiId);
        if (userApi != null) {
            userApiService.deleteUserApiById(apiId);
            return true;
        }
        return false;
    }

    @Override
    public String getUserApiKey(User user, Integer apiId) {
        UserApi userApi = getUserApiByIdCheckOwner(user, apiId);
        return userApi.getApiKey();
    }

    @Override
    public boolean refreshApiKey(User user, Integer apiId) {
        UserApi userApi = getUserApiByIdCheckOwner(user, apiId);
        userApiService.refreshUserApiKey(userApi);
        return true;
    }

    @Override
    public List<SummaryUserApiDto> getUserApisInfo(User user) {
        Integer userId = user.getId();
        List<UserApi> apis = userApiService.getUserApisByUserId(userId);
        List<SummaryUserApiDto> apiDtoList = getSummaryUserApiDtos(apis);
        return apiDtoList;
    }

    @Override
    public void renameUserApi(User user, Integer apiId, String newUserApiName) {
        UserApi userApi = getUserApiByIdCheckOwner(user, apiId);
        appServiceObjectService.renameAppServiceObjectApi(userApi, newUserApiName);
    }

    @Override
    public UserApiDto getUserApiInfo(User user, Integer apiId) {
        UserApi userApi = getUserApiByIdWithRequestsCheckOwner(user, apiId);
        List<UserApiRequestDto> requestDtoList = userApi.getUserApiRequests()
            .stream()
            .map(req -> {
                Integer reqId = req.getId();
                String requestName = req.getRequestName();
                String requestTemplate = req.getRequestTemplate();
                HttpMethod httpMethod = req.getHttpMethod();
                String userApiUrl = userApiUtilsService.getUserApiRequestUrl(req);
                return new UserApiRequestDto(reqId, requestName, requestTemplate, httpMethod, userApiUrl);
            })
            .collect(Collectors.toList());
        UserApiDto userApiDto = getUserApiDto(userApi, requestDtoList);
        return userApiDto;
    }

    private UserApi getUserApiByIdCheckOwner(User user, Integer apiId) {
        UserApi userApi = userApiService.getUserApiByIdWithOwner(apiId);
        appServiceObjectService.isAppServiceObjectOwnerOrThrow(user, userApi);
        return userApi;
    }

    private UserApi getUserApiByIdWithRequestsCheckOwner(User user, Integer apiId) {
        UserApi userApi = userApiService.getUserApiByIdWithRequestsAndOwner(apiId);
        appServiceObjectService.isAppServiceObjectOwnerOrThrow(user, userApi);
        return userApi;
    }

    private List<SummaryUserApiDto> getSummaryUserApiDtos(List<UserApi> apis) {
        List<SummaryUserApiDto> apiDtoList = apis.stream()
            .map(api -> getSummaryUserApiDto(api))
            .collect(Collectors.toList());
        return apiDtoList;
    }

    private UserApiDto getUserApiDto(UserApi userApi, List<UserApiRequestDto> requestDtoList) {
        Integer userApiId = userApi.getId();
        boolean isUserApiProtected = userApi.isProtected();
        String userApiName = userApi.getName();
        Integer userApiDatabaseId = userApi.getDatabase().getId();
        AppServiceObjectStatus appServiceObjectStatus = userApi.getAppServiceObjectStatus();
        AppServiceObjectStatusDto appServiceObjectStatusDto = new AppServiceObjectStatusDto(appServiceObjectStatus);
        Date createdAt = userApi.getCreatedAt();
        int requestsCount = userApiService.getUserApiRequestsCountById(userApiId);
        UserApiDto userApiDto = new UserApiDto(userApiId, userApiName, createdAt, appServiceObjectStatusDto, isUserApiProtected, requestsCount, userApiDatabaseId, requestDtoList);
        return userApiDto;
    }

    private SummaryUserApiDto getSummaryUserApiDto(UserApi userApi) {
        boolean isProtected;
        String userApiName;
        userApiName = userApi.getName();
        isProtected = userApi.isProtected();
        Integer apiId = userApi.getId();
        int requestsCount = userApi.getUserApiRequests().size();
        AppServiceObjectStatus appServiceObjectStatus = userApi.getAppServiceObjectStatus();
        AppServiceObjectStatusDto appServiceObjectStatusDto = new AppServiceObjectStatusDto(appServiceObjectStatus);
        Date createdAt = userApi.getCreatedAt();
        SummaryUserApiDto apiDto = new SummaryUserApiDto(apiId, userApiName, createdAt, appServiceObjectStatusDto, isProtected, requestsCount);
        return apiDto;
    }

}
