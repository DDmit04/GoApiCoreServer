package com.goapi.goapi.service.implementation.appService.userApi;

import com.goapi.goapi.UrlUtils;
import com.goapi.goapi.controller.forms.userApi.request.UserApiRequestData;
import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequest;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequestArgument;
import com.goapi.goapi.exception.userApi.request.UserApiRequestNotFoundException;
import com.goapi.goapi.repo.userApi.ApiRequestRepository;
import com.goapi.goapi.service.implementation.appService.userApi.query.builder.QueryRequestBuilder;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserApiRequestServiceImpl implements UserApiRequestService {

    @Value("${urls.api-request.path.start}")
    private String doRequestUrl;
    @Value("${urls.api-request.path.param-name.apiId}")
    private String apiIdParamName;
    @Value("${urls.api-request.path.param-name.requestId}")
    private String requestIdParamName;
    private final ApiRequestRepository apiRequestRepository;

    @Override
    public UserApiRequest findUserApiRequestById(Integer apiId, Integer requestId) {
        Optional<UserApiRequest> userApiRequestOptional = apiRequestRepository.findByIdAndUserApi_Id(requestId, apiId);
        return userApiRequestOptional.orElseThrow(() -> new UserApiRequestNotFoundException(requestId));
    }

    @Override
    public UserApiRequest findUserApiRequestByIdWithApi(Integer apiId, Integer requestId) {
        //TODO entity graph
        Optional<UserApiRequest> userApiRequestOptional = apiRequestRepository.findByIdAndUserApi_Id(requestId, apiId);
        return userApiRequestOptional.orElseThrow(() -> new UserApiRequestNotFoundException(requestId));
    }

    @Override
    public void deleteUserApiRequestById(Integer requestId) {
        apiRequestRepository.deleteById(requestId);
    }

    @Override
    public UserApiRequest createNewRequest(UserApi userApi, UserApiRequestData userApiRequestData, Set<UserApiRequestArgument> userRequestArguments) {
        HttpMethod httpMethod = userApiRequestData.getHttpMethod();
        String requestTemplate = userApiRequestData.getRequestTemplate();
        String requestName = userApiRequestData.getRequestName();
        UserApiRequest newUserApiRequest = new UserApiRequest(userApi, requestName, requestTemplate, httpMethod, userRequestArguments);
        return apiRequestRepository.save(newUserApiRequest);
    }

    @Override
    public String buildRequestQuery(UserApiRequest userApiRequest, Map<String, Object> arguments) {
        QueryRequestBuilder structure = new QueryRequestBuilder(userApiRequest);
        arguments.entrySet()
            .stream()
            .forEach(entry -> {
                String argName = entry.getKey();
                Object argValue = entry.getValue();
                structure.setArgument(argName, argValue);
            });
        String query = structure.getQuery();
        return query;
    }

    @Override
    public String getUserApiRequestUrl(UserApiRequest userApiRequest) {
        Integer userApiId = userApiRequest.getUserApi().getId();
        Integer requestId = userApiRequest.getId();
        Map<String, String> params = new HashMap<>() {{
            put(apiIdParamName, userApiId.toString());
            put(requestIdParamName, requestId.toString());
        }};
        String finalUrl = UrlUtils.addQueryParamsToUrl(doRequestUrl, params);
        return finalUrl;
    }

    @Override
    public void updateRequestInfo(UserApiRequest userApiRequest, UserApiRequestData userApiRequestData) {
        HttpMethod httpMethod = userApiRequestData.getHttpMethod();
        String requestTemplate = userApiRequestData.getRequestTemplate();
        String requestName = userApiRequestData.getRequestName();
        userApiRequest.setHttpMethod(httpMethod);
        userApiRequest.setRequestName(requestName);
        userApiRequest.setRequestTemplate(requestTemplate);
        apiRequestRepository.save(userApiRequest);
    }

}
