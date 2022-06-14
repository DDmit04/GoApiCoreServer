package com.goapi.goapi.service.implementation.facade.userApi;

import com.goapi.goapi.controller.forms.userApi.CreateUserApiRequest;
import com.goapi.goapi.domain.dto.userApi.SummaryUserApiDto;
import com.goapi.goapi.domain.dto.userApi.UserApiDto;
import com.goapi.goapi.domain.dto.userApi.UserApiRequestDto;
import com.goapi.goapi.domain.model.appService.database.Database;
import com.goapi.goapi.domain.model.appService.tariff.UserApiTariff;
import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequest;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.BillType;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.service.interfaces.appService.database.DatabaseService;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiRequestService;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiService;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiTariffService;
import com.goapi.goapi.service.interfaces.facade.userApi.UserApiServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserApiServiceFacadeImpl implements UserApiServiceFacade {

    private final UserApiService userApiService;
    private final UserApiTariffService userApiTariffService;
    private final DatabaseService databaseService;
    private final UserApiRequestService userApiRequestService;


    @Override
    public SummaryUserApiDto createApi(User user, CreateUserApiRequest createUserApiRequest) {
        Integer dbId = createUserApiRequest.getDatabaseId();
        Integer tariffId = createUserApiRequest.getTariffId();
        boolean isProtected = createUserApiRequest.isProtected();
        String userApiName = createUserApiRequest.getName();

        Database database = databaseService.getDatabaseById(dbId);
        UserApiTariff userApiTariff = userApiTariffService.getUserApiTariffById(tariffId);
        AppServiceBill userApiAppServiceBill = new AppServiceBill(user, BillType.USER_API);
        UserApi userApi = userApiService.createUserApi(userApiName, isProtected, userApiTariff, database, user, userApiAppServiceBill);
        userApiName = userApi.getUserApiName();
        isProtected = userApi.isProtected();
        Integer apiId = userApi.getId();
        int requestsCount = userApi.getUserApiRequests().size();
        SummaryUserApiDto apiDto = new SummaryUserApiDto(apiId, isProtected, userApiName, requestsCount);
        return apiDto;
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
    public boolean renameUserApi(User user, Integer apiId, String newUserApiName) {
        UserApi userApi = getUserApiByIdCheckOwner(user, apiId);
        boolean renamed = userApiService.renameUserApi(userApi, newUserApiName);
        return renamed;
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
                String userApiUrl = userApiRequestService.getUserApiRequestUrl(req);
                return new UserApiRequestDto(reqId, requestName, requestTemplate, httpMethod, userApiUrl);
            })
            .collect(Collectors.toList());
        Integer userApiId = userApi.getId();
        boolean isUserApiProtected = userApi.isProtected();
        String userApiName = userApi.getUserApiName();
        Integer userApiDatabaseId = userApi.getDatabase().getId();
        UserApiDto userApiDto = new UserApiDto(userApiId, isUserApiProtected, userApiName, userApiDatabaseId, requestDtoList);
        return userApiDto;
    }

    private UserApi getUserApiByIdCheckOwner(User user, Integer apiId) {
        UserApi userApi = userApiService.getUserApiByIdWithOwner(apiId);
        userApiService.isApiOwnerOrThrow(user, userApi);
        return userApi;
    }

    private UserApi getUserApiByIdWithRequestsCheckOwner(User user, Integer apiId) {
        UserApi userApi = userApiService.getUserApiByIdWithRequestsAndOwner(apiId);
        userApiService.isApiOwnerOrThrow(user, userApi);
        return userApi;
    }

    private List<SummaryUserApiDto> getSummaryUserApiDtos(List<UserApi> apis) {
        List<SummaryUserApiDto> apiDtoList = apis.stream()
            .map(api -> {
                Integer apiId = api.getId();
                boolean isProtected = api.isProtected();
                String name = api.getUserApiName();
                Set<UserApiRequest> userApiRequests = api.getUserApiRequests();
                int requestsCount = userApiRequests.size();
                return new SummaryUserApiDto(apiId, isProtected, name, requestsCount);
            })
            .collect(Collectors.toList());
        return apiDtoList;
    }

}
