package com.goapi.goapi.service.facase;

import com.fasterxml.jackson.databind.JsonNode;
import com.goapi.goapi.controller.dto.UserApiRequestDto;
import com.goapi.goapi.controller.form.api.CallApiRequest;
import com.goapi.goapi.controller.form.api.CreateApiRequestArgument;
import com.goapi.goapi.controller.form.api.CreateApiRequestRequest;
import com.goapi.goapi.domain.api.UserApi;
import com.goapi.goapi.domain.api.request.UserApiRequest;
import com.goapi.goapi.domain.user.User;
import com.goapi.goapi.service.api.UserApiRequestArgumentService;
import com.goapi.goapi.service.api.UserApiRequestService;
import com.goapi.goapi.service.api.UserApiService;
import com.goapi.goapi.service.grpc.ExternalDatabaseLocationService;
import com.goapi.goapi.service.grpc.ExternalDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserApiRequestServiceFacade {

    private final UserApiRequestService userApiRequestService;
    private final UserApiService userApiService;
    private final UserApiRequestArgumentService userApiRequestArgumentService;
    private final ExternalDatabaseService externalDatabaseService;

    public UserApiRequestDto createNewUserApiRequest(User user, Integer apiId, CreateApiRequestRequest createApiRequestRequest) {
        UserApi userApi = userApiService.getApiByIdCheckOwner(apiId, user);
        if (userApi != null) {
            return createApiRequestRequest(createApiRequestRequest, userApi);
        }
        throw new RuntimeException();
    }

    public boolean deleteUserApiRequest(User user, Integer apiId, Integer requestId) {
        UserApi userApi = userApiService.getApiByIdCheckOwner(apiId, user);
        if (userApi != null) {
            deleteUserApiRequest(requestId);
            return true;
        }
        throw new RuntimeException();
    }

    public UserApiRequestDto updateUserApiRequest(User user, Integer apiId, Integer requestId, CreateApiRequestRequest createApiRequestRequest) {
        UserApi userApi = userApiService.getApiByIdCheckOwner(apiId, user);
        if (userApi != null) {
            deleteUserApiRequest(requestId);
            return createApiRequestRequest(createApiRequestRequest, userApi);
        }
        throw new RuntimeException();
    }

    private UserApiRequestDto createApiRequestRequest(CreateApiRequestRequest createApiRequestRequest, UserApi userApi) {
        HttpMethod httpMethod = createApiRequestRequest.getHttpMethod();
        String requestTemplate = createApiRequestRequest.getRequestTemplate();
        String requestName = createApiRequestRequest.getRequestName();
        UserApiRequest userApiRequest = userApiRequestService.createNewRequest(userApi, requestName, requestTemplate, httpMethod);
        Set<CreateApiRequestArgument> arguments = createApiRequestRequest.getApiRequestArguments();
        validateRequestArgumentsNames(arguments);
        validateRequestTemplate(requestTemplate, arguments);
        userApiRequestArgumentService.saveRequestArguments(userApiRequest, arguments);
        UserApiRequestDto dto = new UserApiRequestDto(userApiRequest.getId(), userApiRequest.getRequestName(),
            userApiRequest.getRequestTemplate(), userApiRequest.getHttpMethod());
        return dto;
    }

    public JsonNode doRequest(Integer apiId, Integer requestId, String method, String apiKey, CallApiRequest callApiRequest) {
        Optional<UserApi> userApi = userApiService.findApiById(apiId);
        return userApi.map(api -> {
            Optional<UserApiRequest> userApiRequestOptional = userApiRequestService.findUserApiRequestById(requestId);
            return userApiRequestOptional.map(userApiRequest -> {
                boolean requestMethodIsMatch = userApiRequest.getHttpMethod().toString().equals(method);
                boolean requestKeyIsMatch = !api.isProtected() || api.getApiKey().equals(apiKey);
                boolean canProcessRequest = requestMethodIsMatch && requestKeyIsMatch;
                if(canProcessRequest) {
                    Map<String, Object> arguments = callApiRequest.getArguments();
                    String finalTemplate = userApiRequestService.buildRequestQuery(userApiRequest, arguments);
                    Integer dbId = api.getDatabase().getId();
                    JsonNode map = externalDatabaseService.sendQuery(dbId, finalTemplate);
                    return map;
                }
                throw new RuntimeException();
            }).orElseThrow(() -> new RuntimeException());
        }).orElseThrow(() -> new RuntimeException());
    }

    private void deleteUserApiRequest(Integer requestId) {
        Optional<UserApiRequest> userApiRequestOptional = userApiRequestService.findUserApiRequestById(requestId);
        userApiRequestOptional.ifPresent(req -> userApiRequestService.deleteUserApiRequestById(requestId));
    }

    private void validateRequestArgumentsNames(Set<CreateApiRequestArgument> arguments) {
        long uniqueArgsCount = arguments.stream()
            .map(arg -> arg.getArgName())
            .distinct()
            .count();
        if(uniqueArgsCount != arguments.size()) {
            throw new RuntimeException();
        }
    }

    private void validateRequestTemplate(String template, Set<CreateApiRequestArgument> arguments) {
        Set<String> argumentNames = arguments.stream()
            .map(arg -> arg.getArgName())
            .collect(Collectors.toSet());
        argumentNames.forEach(argName -> {
            int argUsageIndex = template.indexOf("${" + argName + "}");
            if(argUsageIndex == -1) {
                throw new RuntimeException();
            }
        });
    }

}
