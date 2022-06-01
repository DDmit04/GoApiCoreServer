package com.goapi.goapi.service.interfaces.userApi;

import com.goapi.goapi.domain.model.userApi.UserApi;
import com.goapi.goapi.domain.model.userApi.request.UserApiRequest;
import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.Optional;

public interface UserApiRequestService {
    Optional<UserApiRequest> findUserApiRequestById(Integer requestId);

    void deleteUserApiRequestById(Integer requestId);

    UserApiRequest createNewRequest(UserApi userApi, String requestName, String requestTemplate, HttpMethod httpMethod);

    String buildRequestQuery(UserApiRequest userApiRequest, Map<String, Object> arguments);

    String getUserApiRequestUrl(UserApiRequest userApiRequest);
}
