package com.goapi.goapi.service.implementation.facade.appService.userApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.goapi.goapi.controller.forms.userApi.CallApiRequest;
import com.goapi.goapi.controller.forms.userApi.argument.UpdateApiRequestArgument;
import com.goapi.goapi.controller.forms.userApi.request.UpdateApiRequestRequest;
import com.goapi.goapi.controller.forms.userApi.request.UserApiRequestData;
import com.goapi.goapi.domain.dto.appServiceobject.userApi.UserApiRequestArgumentDto;
import com.goapi.goapi.domain.dto.appServiceobject.userApi.UserApiRequestDto;
import com.goapi.goapi.domain.dto.appServiceobject.userApi.summary.SummaryUserApiRequestDto;
import com.goapi.goapi.domain.model.appService.AppServiceStatusType;
import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import com.goapi.goapi.domain.model.appService.userApi.request.RequestArgumentType;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequest;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequestArgument;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.appService.database.DatabaseDisabledException;
import com.goapi.goapi.exception.appService.userApi.UserApiDisabledException;
import com.goapi.goapi.exception.appService.userApi.request.UserApiRequestKeyException;
import com.goapi.goapi.exception.appService.userApi.request.UserApiRequestMethodException;
import com.goapi.goapi.exception.appService.userApi.request.UserApiRequestNotFoundException;
import com.goapi.goapi.service.interfaces.appService.AppServiceObjectService;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiService;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiUtilsService;
import com.goapi.goapi.service.interfaces.appService.userApi.request.UserApiRequestArgumentService;
import com.goapi.goapi.service.interfaces.appService.userApi.request.UserApiRequestService;
import com.goapi.goapi.service.interfaces.facade.userApi.UserApiRequestServiceFacade;
import com.goapi.goapi.service.interfaces.facade.userApi.UserApiRequestValidationService;
import com.goapi.goapi.service.interfaces.grpc.ExternalDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserApiRequestServiceFacadeImpl implements UserApiRequestServiceFacade {

    private final UserApiRequestService userApiRequestService;
    private final UserApiService userApiService;
    private final AppServiceObjectService appServiceObjectService;
    private final UserApiRequestArgumentService userApiRequestArgumentService;
    private final ExternalDatabaseService externalDatabaseService;
    private final UserApiUtilsService userApiUtilsService;
    private final UserApiRequestValidationService userApiRequestValidationService;

    @Override
    public SummaryUserApiRequestDto createUserApiRequest(User user, Integer apiId, UserApiRequestData createApiRequestRequest) {
        UserApi userApi = getUserApiByIdCheckOwnerWithTariff(user, apiId);
        userApiRequestValidationService.validateRequestDataOnRequestCreate(userApi, createApiRequestRequest);
        UserApiRequest newUserApiRequest = userApiRequestService.createNewRequest(userApi, createApiRequestRequest);
        SummaryUserApiRequestDto newSummaryUserApiRequestDto = getSummaryUserApiRequestDto(newUserApiRequest);
        return newSummaryUserApiRequestDto;
    }

    @Override
    public boolean deleteUserApiRequest(User user, Integer apiId, Integer requestId) {
        UserApiRequest userApiRequest = getUserApiRequestByIdCheckOwner(user, apiId, requestId);
        Integer userApiRequestId = userApiRequest.getId();
        userApiRequestService.deleteUserApiRequestById(userApiRequestId);
        return true;
    }

    @Override
    public SummaryUserApiRequestDto updateUserApiRequest(User user, Integer apiId, UpdateApiRequestRequest updateApiRequestRequest) {
        Integer requestId = updateApiRequestRequest.getId();
        UserApiRequest userApiRequest = getUserApiRequestByIdCheckOwner(user, apiId, requestId);
        Set<UpdateApiRequestArgument> arguments = updateApiRequestRequest.getApiRequestArguments();
        userApiRequestValidationService.validateRequestDataOnRequestUpdate(updateApiRequestRequest);
        userApiRequestService.updateRequestInfo(userApiRequest, updateApiRequestRequest);
        userApiRequestArgumentService.updateRequestArguments(userApiRequest, arguments);
        SummaryUserApiRequestDto updatedSummaryUserApiRequestDto = getSummaryUserApiRequestDto(userApiRequest);
        return updatedSummaryUserApiRequestDto;
    }

    @Override
    public JsonNode doUserApiRequest(Integer apiId, Integer requestId, String method, String apiKey, CallApiRequest callApiRequest) {
        UserApi userApi = userApiService.getUserApiById(apiId);
        UserApiRequest userApiRequest = userApiRequestService.findUserApiRequestByIdWithArguments(requestId);
        HttpMethod requestHttpMethod = userApiRequest.getHttpMethod();
        Integer dbId = userApi.getDatabase().getId();
        boolean databaseDisabled = userApi.getDatabase().getAppServiceObjectStatus().getStatus() == AppServiceStatusType.DISABLED;
        boolean apiIsDisabled = userApi.getAppServiceObjectStatus().getStatus() == AppServiceStatusType.DISABLED;
        boolean anyApiComponentIsDisabled = databaseDisabled || apiIsDisabled;
        boolean requestMethodIsMatch = requestHttpMethod.toString().equals(method);
        boolean apiIsProtected = userApi.isProtected();
        boolean apiKeyMatches = userApi.getApiKey().equals(apiKey);
        boolean canAccessApi = !apiIsProtected || apiKeyMatches;
        boolean canProcessRequest = requestMethodIsMatch && canAccessApi && !anyApiComponentIsDisabled;
        if (canProcessRequest) {
            Map<String, Object> arguments = callApiRequest.getArguments();
            String databaseQuery = userApiUtilsService.buildUserApiRequestQuery(userApiRequest, arguments);
            JsonNode requestResult = externalDatabaseService.sendQuery(dbId, databaseQuery);
            return requestResult;
        } else if (!requestMethodIsMatch) {
            throw new UserApiRequestMethodException(requestId, method);
        } else if (databaseDisabled) {
            throw new DatabaseDisabledException(dbId);
        } else if (apiIsDisabled) {
            Integer userApiId = userApi.getId();
            throw new UserApiDisabledException(userApiId);
        } else {
            throw new UserApiRequestKeyException(requestId);
        }
    }

    @Override
    public List<SummaryUserApiRequestDto> getUserApiRequests(User user, Integer apiId) {
        UserApi userApi = getUserApiByIdWithRequestsCheckOwner(user, apiId);
        Set<UserApiRequest> userApiRequests = userApi.getUserApiRequests();
        List<SummaryUserApiRequestDto> summaryUserApiRequestDtoList = userApiRequests
            .stream()
            .map(req -> getSummaryUserApiRequestDto(req))
            .collect(Collectors.toList());
        return summaryUserApiRequestDtoList;
    }

    @Override
    public UserApiRequestDto getUserApiRequestInfo(User user, Integer apiId, Integer requestId) {
        getUserApiByIdCheckOwner(user, apiId);
        UserApiRequest userApiRequest = userApiRequestService.findUserApiRequestByIdAndApiIdWithArguments(apiId, requestId);
        UserApiRequestDto apiRequestArgumentDto = getUserApiRequestDto(userApiRequest);
        return apiRequestArgumentDto;
    }

    private UserApi getUserApiByIdWithRequestsCheckOwner(User user, Integer apiId) {
        UserApi userApi = userApiService.getUserApiByIdWithRequestsAndOwner(apiId);
        checkUserApiOwner(user, userApi);
        return userApi;
    }

    private UserApi getUserApiByIdCheckOwnerWithTariff(User user, Integer apiId) {
        UserApi userApi = userApiService.getUserApiByIdWithTariffAndOwner(apiId);
        checkUserApiOwner(user, userApi);
        return userApi;
    }

    private UserApi getUserApiByIdCheckOwner(User user, Integer apiId) {
        UserApi userApi = userApiService.getUserApiByIdWithOwner(apiId);
        checkUserApiOwner(user, userApi);
        return userApi;
    }

    private void checkUserApiOwner(User user, UserApi userApi) {
        appServiceObjectService.isAppServiceObjectOwnerOrThrow(user, userApi);
    }

    private UserApiRequest getUserApiRequestByIdCheckOwner(User user, Integer apiId, Integer requestId) {
        UserApi userApi = getUserApiByIdWithRequestsCheckOwner(user, apiId);
        UserApiRequest userApiRequest = userApi
            .getUserApiRequests()
            .stream()
            .filter(rec -> rec.getId() == requestId)
            .findFirst()
            .orElseThrow(() -> new UserApiRequestNotFoundException(apiId, requestId));
        return userApiRequest;
    }

    private SummaryUserApiRequestDto getSummaryUserApiRequestDto(UserApiRequest updatedApiRequestRequest) {
        Integer userApiRequestId = updatedApiRequestRequest.getId();
        String userApiRequestName = updatedApiRequestRequest.getRequestName();
        String userApiRequestTemplate = updatedApiRequestRequest.getRequestTemplate();
        HttpMethod userApiRequestMethod = updatedApiRequestRequest.getHttpMethod();
        String userApiRequestUrl = userApiUtilsService.getUserApiRequestUrl(updatedApiRequestRequest);
        SummaryUserApiRequestDto updatedSummaryUserApiRequestDto = new SummaryUserApiRequestDto(
            userApiRequestId,
            userApiRequestName,
            userApiRequestTemplate,
            userApiRequestMethod,
            userApiRequestUrl
        );
        return updatedSummaryUserApiRequestDto;
    }

    private UserApiRequestDto getUserApiRequestDto(UserApiRequest userApiRequest) {
        Set<UserApiRequestArgument> arguments = userApiRequest.getUserApiRequestArguments();
        List<UserApiRequestArgumentDto> argumentsDto = arguments
            .stream()
            .map(arg -> {
                Integer argId = arg.getId();
                String argName = arg.getArgName();
                RequestArgumentType argType = arg.getRequestArgumentType();
                return new UserApiRequestArgumentDto(argId, argName, argType);
            })
            .collect(Collectors.toList());
        SummaryUserApiRequestDto summaryUserApiRequestDto = getSummaryUserApiRequestDto(userApiRequest);
        UserApiRequestDto apiRequestArgumentDto = new UserApiRequestDto(summaryUserApiRequestDto, argumentsDto);
        return apiRequestArgumentDto;
    }

}
