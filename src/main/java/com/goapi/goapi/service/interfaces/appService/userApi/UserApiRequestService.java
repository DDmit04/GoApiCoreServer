package com.goapi.goapi.service.interfaces.appService.userApi;

import com.goapi.goapi.controller.forms.userApi.request.UserApiRequestData;
import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequest;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequestArgument;

import java.util.Map;
import java.util.Set;

public interface UserApiRequestService {
    UserApiRequest findUserApiRequestById(Integer apiId, Integer requestId);

    void deleteUserApiRequestById(Integer requestId);

    UserApiRequest createNewRequest(UserApi userApi, UserApiRequestData userApiRequestData, Set<UserApiRequestArgument> userRequestArguments);

    String buildRequestQuery(UserApiRequest userApiRequest, Map<String, Object> arguments);

    String getUserApiRequestUrl(UserApiRequest userApiRequest);

    void updateRequestInfo(UserApiRequest userApiRequest, UserApiRequestData userApiRequestData);

    UserApiRequest findUserApiRequestByIdWithApi(Integer apiId, Integer requestId);
}
