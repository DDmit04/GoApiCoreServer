package com.goapi.goapi.service.interfaces.appService.userApi.request;

import com.goapi.goapi.controller.forms.userApi.request.UserApiRequestData;
import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequest;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequestArgument;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
public interface UserApiRequestService {

    UserApiRequest findUserApiRequestByIdAndApiId(Integer apiId, Integer requestId);
    UserApiRequest findUserApiRequestByIdWithArguments(Integer apiId, Integer requestId);
    void deleteUserApiRequestById(Integer requestId);
    UserApiRequest createNewRequest(UserApi userApi, UserApiRequestData userApiRequestData, Set<UserApiRequestArgument> userRequestArguments);
    void updateRequestInfo(UserApiRequest userApiRequest, UserApiRequestData userApiRequestData);
}
