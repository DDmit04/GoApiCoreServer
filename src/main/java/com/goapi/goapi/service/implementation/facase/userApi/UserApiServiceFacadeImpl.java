package com.goapi.goapi.service.implementation.facase.userApi;

import com.goapi.goapi.controller.forms.api.CreateUserApiRequest;
import com.goapi.goapi.domain.dto.api.SummaryUserApiDto;
import com.goapi.goapi.domain.dto.api.UserApiDto;
import com.goapi.goapi.domain.dto.api.UserApiRequestDto;
import com.goapi.goapi.domain.model.bill.Bill;
import com.goapi.goapi.domain.model.database.Database;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.userApi.UserApi;
import com.goapi.goapi.exception.database.DatabaseNotFoundException;
import com.goapi.goapi.service.implementation.BillService;
import com.goapi.goapi.service.interfaces.database.DatabaseService;
import com.goapi.goapi.service.interfaces.facase.userApi.UserApiServiceFacade;
import com.goapi.goapi.service.interfaces.userApi.UserApiRequestService;
import com.goapi.goapi.service.interfaces.userApi.UserApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserApiServiceFacadeImpl implements UserApiServiceFacade {

    private final UserApiService userApiService;
    private final DatabaseService databaseService;
    private final UserApiRequestService userApiRequestService;

    private final BillService billService;


    @Override
    public SummaryUserApiDto createApi(User user, CreateUserApiRequest createUserApiRequest) {
        Integer dbId = createUserApiRequest.getDatabaseId();
        Optional<Database> databaseOptional = databaseService.getDatabaseById(dbId);
        return databaseOptional.map(database -> {
            boolean isProtected = createUserApiRequest.isProtected();
            String userApiName = createUserApiRequest.getName();
            Bill userApiBill = billService.createUserApiBill(user);
            UserApi userApi = userApiService.createUserApi(userApiName, isProtected, database, user, userApiBill);
            userApiName = userApi.getUserApiName();
            isProtected = userApi.isProtected();
            Integer apiId = userApi.getId();
            int requestsCount = userApi.getUserApiRequests().size();
            SummaryUserApiDto apiDto = new SummaryUserApiDto(apiId, isProtected, userApiName, requestsCount);
            return apiDto;
        }).orElseThrow(() -> new DatabaseNotFoundException(dbId));

    }

    @Override
    public boolean deleteApi(User user, Integer apiId) {
        UserApi userApi = userApiService.getApiByIdCheckOwner(user, apiId);
        if (userApi != null) {
            userApiService.deleteUserApiById(apiId);
            return true;
        }
        return false;
    }

    @Override
    public String getUserApiKey(User user, Integer apiId) {
        UserApi userApi = userApiService.getApiByIdCheckOwner(user, apiId);
        return userApi. getApiKey();
    }

    @Override
    public boolean refreshApiKey(User user, Integer apiId) {
        UserApi userApi = userApiService.getApiByIdCheckOwner(user, apiId);
        userApiService.refreshUserApiKey(userApi);
        return true;
    }

    @Override
    public List<SummaryUserApiDto> listUserApis(User user) {
        List<UserApi> apis = userApiService.getUserApisByUserId(user.getId());
        List<SummaryUserApiDto> apiDtoList = apis.stream()
            .map(api -> {
                Integer apiId = api.getId();
                boolean isProtected = api.isProtected();
                String name = api.getUserApiName();
                int requestsCount = api.getUserApiRequests().size();
                return new SummaryUserApiDto(apiId, isProtected, name, requestsCount);
            })
            .collect(Collectors.toList());
        return apiDtoList;
    }

    @Override
    public boolean renameUserApi(User user, Integer apiId, String newUserApiName) {
        UserApi userApi = userApiService.getApiByIdCheckOwner(user, apiId);
        boolean renamed = userApiService.renameUserApi(userApi, newUserApiName);
        return renamed;
    }

    @Override
    public UserApiDto getUserApiInfo(User user, Integer apiId) {
        UserApi userApi = userApiService.getApiByIdCheckOwner(user, apiId);
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
        Integer userApiId = user.getId();
        boolean isUserApiProtected = userApi.isProtected();
        String userApiName = userApi.getUserApiName();
        UserApiDto userApiDto = new UserApiDto(userApiId, isUserApiProtected, userApiName, requestDtoList);
        return userApiDto;
    }

}
