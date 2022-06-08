package com.goapi.goapi.service.implementation.userApi;

import com.goapi.goapi.UrlUtils;
import com.goapi.goapi.domain.model.userApi.UserApi;
import com.goapi.goapi.domain.model.userApi.request.UserApiRequest;
import com.goapi.goapi.repo.userApi.ApiRequestRepository;
import com.goapi.goapi.service.implementation.userApi.query.builder.QueryRequestBuilder;
import com.goapi.goapi.service.interfaces.userApi.UserApiRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    public Optional<UserApiRequest> findUserApiRequestById(Integer requestId) {
        return apiRequestRepository.findById(requestId);
    }

    @Override
    public void deleteUserApiRequestById(Integer requestId) {
        apiRequestRepository.deleteById(requestId);
    }

    @Override
    public UserApiRequest createNewRequest(UserApi userApi, String requestName, String requestTemplate, HttpMethod httpMethod) {
        UserApiRequest newUserApiRequest = new UserApiRequest(userApi, requestName, requestTemplate, httpMethod);
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
        UserApi userApi = userApiRequest.getUserApi();
        Integer userApiId = userApi.getId();
        Integer requestId = userApiRequest.getId();
        Map<String, String> params = new HashMap<>(){{
            put(apiIdParamName, userApiId.toString());
            put(requestIdParamName, requestId.toString());
        }};
        String finalUrl = UrlUtils.addQueryParamsToUrl(doRequestUrl, params);
        return finalUrl;
    }

}
