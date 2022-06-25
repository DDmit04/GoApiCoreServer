package com.goapi.goapi.service.interfaces.appService.userApi;

import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequest;

import java.util.Map;

public interface UserApiUtilsService {

    String buildUserApiRequestQuery(UserApiRequest userApiRequest, Map<String, Object> arguments);
    String getUserApiRequestUrl(UserApiRequest userApiRequest);
}
