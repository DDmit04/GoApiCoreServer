package com.goapi.goapi.service.implementation.facase.userApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.goapi.goapi.controller.forms.api.CallApiRequest;
import com.goapi.goapi.controller.forms.api.argument.CreateApiRequestArgument;
import com.goapi.goapi.controller.forms.api.argument.UpdateApiRequestArgument;
import com.goapi.goapi.controller.forms.api.argument.UserApiRequestArgumentData;
import com.goapi.goapi.controller.forms.api.request.CreateApiRequestRequest;
import com.goapi.goapi.controller.forms.api.request.UpdateApiRequestRequest;
import com.goapi.goapi.domain.dto.api.UserApiRequestDto;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.userApi.UserApi;
import com.goapi.goapi.domain.model.userApi.UserApiTariff;
import com.goapi.goapi.domain.model.userApi.request.UserApiRequest;
import com.goapi.goapi.exception.userApi.UserApiOwnerException;
import com.goapi.goapi.exception.userApi.UserApiRequestsCountCupException;
import com.goapi.goapi.exception.userApi.request.UserApiRequestArgNotUsedTemplateException;
import com.goapi.goapi.exception.userApi.request.UserApiRequestKeyException;
import com.goapi.goapi.exception.userApi.request.UserApiRequestMethodException;
import com.goapi.goapi.exception.userApi.request.UserApiRequestNotFoundException;
import com.goapi.goapi.exception.userApi.request.UserApiRequestProcessingException;
import com.goapi.goapi.exception.userApi.requestArgument.UserApiRequestArgumentInvalidNameException;
import com.goapi.goapi.exception.userApi.requestArgument.UserApiRequestArgumentsNotUniqueException;
import com.goapi.goapi.service.interfaces.facase.userApi.UserApiRequestServiceFacade;
import com.goapi.goapi.service.interfaces.grpc.ExternalDatabaseService;
import com.goapi.goapi.service.interfaces.userApi.UserApiRequestArgumentService;
import com.goapi.goapi.service.interfaces.userApi.UserApiRequestService;
import com.goapi.goapi.service.interfaces.userApi.UserApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final UserApiRequestArgumentService userApiRequestArgumentService;
    private final ExternalDatabaseService externalDatabaseService;

    @Override
    public UserApiRequestDto createUserApiRequest(User user, Integer apiId, CreateApiRequestRequest createApiRequestRequest) {
        UserApi userApi = getUserApiByIdWithRequestsCheckOwner(user, apiId);
        List<CreateApiRequestArgument> arguments = createApiRequestRequest.getApiRequestArguments();
        validateRequestDataOnCreate(user, createApiRequestRequest);
        UserApiRequest newUserApiRequest = userApiRequestService.createNewRequest(userApi, createApiRequestRequest);
        userApiRequestArgumentService.saveRequestArguments(newUserApiRequest, arguments);
        UserApiRequestDto newUserApiRequestDto = getUserApiRequestDto(newUserApiRequest);
        return newUserApiRequestDto;
    }

    @Override
    public boolean deleteUserApiRequest(User user, Integer apiId, Integer requestId) {
        UserApiRequest userApiRequest = getUserApiRequestByIdCheckOwner(user, apiId, requestId);
        Integer userApiRequestId = userApiRequest.getId();
        userApiRequestService.deleteUserApiRequestById(userApiRequestId);
        return true;
    }

    @Override
    public UserApiRequestDto updateUserApiRequest(User user, Integer apiId, UpdateApiRequestRequest updateApiRequestRequest) {
        Integer requestId = updateApiRequestRequest.getId();
        UserApiRequest userApiRequest = getUserApiRequestByIdCheckOwner(user, apiId, requestId);
        Set<UpdateApiRequestArgument> arguments = updateApiRequestRequest.getApiRequestArguments();
        validateRequestDataOnUpdate(updateApiRequestRequest);
        userApiRequestService.updateRequestInfo(userApiRequest, updateApiRequestRequest);
        userApiRequestArgumentService.updateRequestArguments(userApiRequest, arguments);
        UserApiRequestDto updatedUserApiRequestDto = getUserApiRequestDto(userApiRequest);
        return updatedUserApiRequestDto;
    }

    @Override
    public JsonNode doUserApiRequest(Integer apiId, Integer requestId, String method, String apiKey, CallApiRequest callApiRequest) {
        UserApi userApi = userApiService.getUserApiById(apiId);
        UserApiRequest userApiRequest = userApiRequestService.findUserApiRequestById(requestId);
        HttpMethod requestHttpMethod = userApiRequest.getHttpMethod();
        boolean requestMethodIsMatch = requestHttpMethod.toString().equals(method);
        boolean requestKeyIsMatch = !userApi.isProtected() || userApi.getApiKey().equals(apiKey);
        boolean canProcessRequest = requestMethodIsMatch && requestKeyIsMatch;
        if (canProcessRequest) {
            Map<String, Object> arguments = callApiRequest.getArguments();
            String finalTemplate = userApiRequestService.buildRequestQuery(userApiRequest, arguments);
            Integer dbId = userApi.getDatabase().getId();
            JsonNode requestResult = externalDatabaseService.sendQuery(dbId, finalTemplate);
            return requestResult;
        } else if (!requestKeyIsMatch) {
            throw new UserApiRequestMethodException(requestId, method);
        } else if (!requestMethodIsMatch) {
            throw new UserApiRequestKeyException(requestId);
        }
        throw new UserApiRequestProcessingException(requestId);
    }

    @Override
    public List<UserApiRequestDto> getUserApiRequests(User user, Integer apiId) {
        UserApi userApi = getUserApiByIdWithRequestsCheckOwner(user, apiId);
        Set<UserApiRequest> userApiRequests = userApi.getUserApiRequests();
        List<UserApiRequestDto> userApiRequestDtoList = userApiRequests
            .stream()
            .map(req -> getUserApiRequestDto(req))
            .collect(Collectors.toList());
        return userApiRequestDtoList;
    }

    @Override
    public UserApiRequestDto getUserApiRequestInfo(User user, Integer apiId, Integer requestId) {
        UserApiRequest userApiRequest = userApiRequestService.findUserApiRequestById(requestId);
        UserApi userApi = userApiRequest.getUserApi();
        User owner = userApi.getOwner();
        if (!owner.equals(user)) {
            Integer userId = user.getId();
            throw new UserApiOwnerException(userId, apiId);
        }
        UserApiRequestDto userApiRequestDto = getUserApiRequestDto(userApiRequest);
        return userApiRequestDto;
    }

    private void validateRequestDataOnCreate(User user, CreateApiRequestRequest createApiRequestRequest) {
        List<CreateApiRequestArgument> arguments = createApiRequestRequest.getApiRequestArguments();
        String requestTemplate = createApiRequestRequest.getRequestTemplate();
        validateTotalRequestsCount(user);
        validateRequestTemplate(requestTemplate, arguments);
    }

    private void validateRequestDataOnUpdate(UpdateApiRequestRequest updateApiRequestRequest) {
        Set<UpdateApiRequestArgument> arguments = updateApiRequestRequest.getApiRequestArguments();
        String requestTemplate = updateApiRequestRequest.getRequestTemplate();
        validateRequestTemplate(requestTemplate, arguments);
    }
    private void validateRequestTemplate(String requestTemplate, Collection<? extends UserApiRequestArgumentData> arguments) {
        validateRequestArgumentsNamesLength(arguments);
        validateRequestArgumentsUniqueNames(arguments);
        validateRequestTemplateArguments(requestTemplate, arguments);
    }

    private void validateRequestArgumentsUniqueNames(Collection<? extends UserApiRequestArgumentData> arguments) {
        List<String> uniqueArgsNames = arguments.stream()
            .map(arg -> arg.getArgName())
            .distinct()
            .collect(Collectors.toList());
        int uniqueArgsCount = uniqueArgsNames.size();
        if (uniqueArgsCount != arguments.size()) {
            arguments.removeAll(uniqueArgsNames);
            List<String> duplicateArgsNames = arguments.stream()
                .map(arg -> arg.getArgName())
                .distinct()
                .collect(Collectors.toList());
            throw new UserApiRequestArgumentsNotUniqueException(duplicateArgsNames);
        }
    }

    private void validateRequestArgumentsNamesLength(Collection<? extends UserApiRequestArgumentData> arguments) {
        Optional<? extends UserApiRequestArgumentData> zeroLengthNamesArgs = arguments
            .stream()
            .filter(arg -> !StringUtils.hasLength(arg.getArgName()))
            .findAny();
        if (zeroLengthNamesArgs.isPresent()) {
            throw new UserApiRequestArgumentInvalidNameException();
        }
    }

    private void validateRequestTemplateArguments(String template, Collection<? extends UserApiRequestArgumentData> arguments) {
        Set<String> argumentNames = arguments.stream()
            .map(arg -> arg.getArgName())
            .collect(Collectors.toSet());
        argumentNames.forEach(argName -> {
            int argUsageIndex = template.indexOf("{" + argName + "}");
            if (argUsageIndex == -1) {
                throw new UserApiRequestArgNotUsedTemplateException(argName);
            }
        });
    }

    private void validateTotalRequestsCount(User user) {
        UserApiTariff userApiTariff = user.getUserApiTariff();
        Integer maxRequestsCount = userApiTariff.getMaxRequestsCount();
        Integer userId = user.getId();
        int totalUserRequestsCount = userApiService.getTotalUserApisRequestsCount(userId);
        if (totalUserRequestsCount == maxRequestsCount) {
            throw new UserApiRequestsCountCupException(userId);
        }
    }

    private UserApi getUserApiByIdWithRequestsCheckOwner(User user, Integer apiId) {
        UserApi userApi = userApiService.getUserApiById(apiId);
        userApiService.isApiOwnerOrThrow(user, userApi);
        return userApi;
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

    private UserApiRequestDto getUserApiRequestDto(UserApiRequest updatedApiRequestRequest) {
        Integer userApiRequestId = updatedApiRequestRequest.getId();
        String userApiRequestName = updatedApiRequestRequest.getRequestName();
        String userApiRequestTemplate = updatedApiRequestRequest.getRequestTemplate();
        HttpMethod userApiRequestMethod = updatedApiRequestRequest.getHttpMethod();
        String userApiRequestUrl = userApiRequestService.getUserApiRequestUrl(updatedApiRequestRequest);
        UserApiRequestDto updatedUserApiRequestDto = new UserApiRequestDto(
            userApiRequestId,
            userApiRequestName,
            userApiRequestTemplate,
            userApiRequestMethod,
            userApiRequestUrl
        );
        return updatedUserApiRequestDto;
    }
}
