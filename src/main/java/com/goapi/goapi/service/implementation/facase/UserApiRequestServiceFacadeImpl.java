package com.goapi.goapi.service.implementation.facase;

import com.fasterxml.jackson.databind.JsonNode;
import com.goapi.goapi.controller.form.api.CallApiRequest;
import com.goapi.goapi.controller.form.api.CreateApiRequestArgument;
import com.goapi.goapi.controller.form.api.CreateApiRequestRequest;
import com.goapi.goapi.domain.dto.api.UserApiRequestDto;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.userApi.UserApi;
import com.goapi.goapi.domain.model.userApi.UserApiTariff;
import com.goapi.goapi.domain.model.userApi.request.UserApiRequest;
import com.goapi.goapi.exception.userApi.UserApiNotFoundException;
import com.goapi.goapi.exception.userApi.UserApiOwnerException;
import com.goapi.goapi.exception.userApi.UserApiRequestsCountCupException;
import com.goapi.goapi.exception.userApi.request.UserApiRequestArgNotUsedTemplateException;
import com.goapi.goapi.exception.userApi.request.UserApiRequestArgumentInvalidNameException;
import com.goapi.goapi.exception.userApi.request.UserApiRequestArgumentsNotUniqueException;
import com.goapi.goapi.exception.userApi.request.UserApiRequestKeyException;
import com.goapi.goapi.exception.userApi.request.UserApiRequestMethodException;
import com.goapi.goapi.exception.userApi.request.UserApiRequestNotFoundException;
import com.goapi.goapi.exception.userApi.request.UserApiRequestProcessingException;
import com.goapi.goapi.service.interfaces.facase.UserApiRequestServiceFacade;
import com.goapi.goapi.service.interfaces.grpc.ExternalDatabaseService;
import com.goapi.goapi.service.interfaces.userApi.UserApiRequestArgumentService;
import com.goapi.goapi.service.interfaces.userApi.UserApiRequestService;
import com.goapi.goapi.service.interfaces.userApi.UserApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public UserApiRequestDto createNewUserApiRequest(User user, Integer apiId, CreateApiRequestRequest createApiRequestRequest) {
        UserApi userApi = userApiService.getApiByIdCheckOwner(user, apiId);
        UserApiRequest newUserApiRequest = createApiRequestRequest(user, createApiRequestRequest, userApi);
        String requestUrl = userApiRequestService.getUserApiRequestUrl(newUserApiRequest);
        UserApiRequestDto newUserApiRequestDto = new UserApiRequestDto(
            newUserApiRequest.getId(),
            newUserApiRequest.getRequestName(),
            newUserApiRequest.getRequestTemplate(),
            newUserApiRequest.getHttpMethod(),
            requestUrl
        );
        return newUserApiRequestDto;
    }

    @Override
    public boolean deleteUserApiRequest(User user, Integer apiId, Integer requestId) {
        userApiService.getApiByIdCheckOwner(user, apiId);
        deleteUserApiRequest(requestId);
        return true;
    }

    @Override
    public UserApiRequestDto updateUserApiRequest(User user, Integer apiId, Integer requestId, CreateApiRequestRequest createApiRequestRequest) {
        //TODO make merge instead rewrite
        UserApi userApi = userApiService.getApiByIdCheckOwner(user, apiId);
        deleteUserApiRequest(requestId);
        UserApiRequest updatedApiRequestRequest = createApiRequestRequest(user, createApiRequestRequest, userApi);
        UserApiRequestDto updatedUserApiRequestDto = getUserApiRequestDto(updatedApiRequestRequest);
        return updatedUserApiRequestDto;
    }

    private UserApiRequest createApiRequestRequest(User user, CreateApiRequestRequest createApiRequestRequest, UserApi userApi) {
        HttpMethod httpMethod = createApiRequestRequest.getHttpMethod();
        String requestTemplate = createApiRequestRequest.getRequestTemplate();
        String requestName = createApiRequestRequest.getRequestName();
        Set<CreateApiRequestArgument> arguments = createApiRequestRequest.getApiRequestArguments();
        validateTotalRequestsCount(user);
        validateRequestArgumentsNamesLength(arguments);
        validateRequestArgumentsUniqueNames(arguments);
        validateRequestTemplate(requestTemplate, arguments);
        UserApiRequest userApiRequest = userApiRequestService.createNewRequest(userApi, requestName, requestTemplate, httpMethod);
        userApiRequestArgumentService.saveRequestArguments(userApiRequest, arguments);
        return userApiRequest;
    }

    @Override
    public JsonNode doRequest(Integer apiId, Integer requestId, String method, String apiKey, CallApiRequest callApiRequest) {
        Optional<UserApi> userApi = userApiService.getUserApiById(apiId);
        return userApi.map(api -> {
            Optional<UserApiRequest> userApiRequestOptional = userApiRequestService.findUserApiRequestById(requestId);
            return userApiRequestOptional.map(userApiRequest -> {
                HttpMethod requestHttpMethod = userApiRequest.getHttpMethod();
                boolean requestMethodIsMatch = requestHttpMethod.toString().equals(method);
                boolean requestKeyIsMatch = !api.isProtected() || api.getApiKey().equals(apiKey);
                boolean canProcessRequest = requestMethodIsMatch && requestKeyIsMatch;
                if (canProcessRequest) {
                    Map<String, Object> arguments = callApiRequest.getArguments();
                    String finalTemplate = userApiRequestService.buildRequestQuery(userApiRequest, arguments);
                    Integer dbId = api.getDatabase().getId();
                    JsonNode requestResult = externalDatabaseService.sendQuery(dbId, finalTemplate);
                    return requestResult;
                } else if (!requestKeyIsMatch) {
                    throw new UserApiRequestMethodException(requestId, method);
                } else if (!requestMethodIsMatch) {
                    throw new UserApiRequestKeyException(requestId);
                }
                throw new UserApiRequestProcessingException(requestId);
            }).orElseThrow(() -> new UserApiRequestNotFoundException(requestId));
        }).orElseThrow(() -> new UserApiNotFoundException(apiId));
    }

    @Override
    public List<UserApiRequestDto> getUserApiRequests(User user, Integer apiId) {
        UserApi userApi = userApiService.getApiByIdCheckOwner(user, apiId);
        Set<UserApiRequest> userApiRequests = userApi.getUserApiRequests();
        List<UserApiRequestDto> userApiRequestDtoList = userApiRequests
            .stream()
            .map(req -> getUserApiRequestDto(req))
            .collect(Collectors.toList());
        return userApiRequestDtoList;
    }

    @Override
    public UserApiRequestDto getUserRequestInfo(User user, Integer apiId, Integer requestId) {
        Optional<UserApiRequest> userApiRequestOptional = userApiRequestService.findUserApiRequestById(requestId);
        return userApiRequestOptional.map(userApiRequest -> {
            UserApi userApi = userApiRequest.getUserApi();
            User owner = userApi.getOwner();
            if (!owner.equals(user)) {
                Integer userId = user.getId();
                throw new UserApiOwnerException(userId, apiId);
            }
            UserApiRequestDto userApiRequestDto = getUserApiRequestDto(userApiRequest);
            return userApiRequestDto;
        }).orElseThrow(() -> new UserApiRequestNotFoundException(requestId));
    }

    private void deleteUserApiRequest(Integer requestId) {
        Optional<UserApiRequest> userApiRequestOptional = userApiRequestService.findUserApiRequestById(requestId);
        userApiRequestOptional.ifPresent(req -> userApiRequestService.deleteUserApiRequestById(requestId));
    }

    private void validateRequestArgumentsUniqueNames(Set<CreateApiRequestArgument> arguments) {
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

    private void validateRequestArgumentsNamesLength(Set<CreateApiRequestArgument> arguments) {
        Optional<CreateApiRequestArgument> zeroLengthNamesArgs = arguments
            .stream()
            .filter(arg -> !StringUtils.hasLength(arg.getArgName()))
            .findAny();
        if (zeroLengthNamesArgs.isPresent()) {
            throw new UserApiRequestArgumentInvalidNameException();
        }
    }

    private void validateRequestTemplate(String template, Set<CreateApiRequestArgument> arguments) {
        Set<String> argumentNames = arguments.stream()
            .map(arg -> arg.getArgName())
            .collect(Collectors.toSet());
        argumentNames.forEach(argName -> {
            int argUsageIndex = template.indexOf("${" + argName + "}");
            if (argUsageIndex == -1) {
                throw new UserApiRequestArgNotUsedTemplateException(argName);
            }
        });
    }

    private void validateTotalRequestsCount(User user) {
        UserApiTariff userApiTariff = user.getUserApiTariff();
        Integer maxRequestsCount = userApiTariff.getMaxRequestsCount();
        Integer userId = user.getId();
        int totalUserRequestsCount = userApiService.getTotalUserApiRequestsCount(userId);
        if (totalUserRequestsCount == maxRequestsCount) {
            throw new UserApiRequestsCountCupException(userId);
        }
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
