package com.goapi.goapi.service.interfaces.userApi;

import com.goapi.goapi.controller.forms.api.request.UserApiRequestData;
import com.goapi.goapi.domain.model.userApi.UserApi;
import com.goapi.goapi.domain.model.userApi.request.UserApiRequest;

import java.util.Map;

public interface UserApiRequestService {
    UserApiRequest findUserApiRequestById(Integer requestId);

    void deleteUserApiRequestById(Integer requestId);

    UserApiRequest createNewRequest(UserApi userApi, UserApiRequestData userApiRequestData);

    String buildRequestQuery(UserApiRequest userApiRequest, Map<String, Object> arguments);

    String getUserApiRequestUrl(UserApiRequest userApiRequest);

    void updateRequestInfo(UserApiRequest userApiRequest, UserApiRequestData userApiRequestData);
}
