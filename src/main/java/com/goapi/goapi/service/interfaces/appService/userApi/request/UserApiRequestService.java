package com.goapi.goapi.service.interfaces.appService.userApi.request;

import com.goapi.goapi.controller.forms.userApi.request.UserApiRequestData;
import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserApiRequestService {

    UserApiRequest findUserApiRequestByIdWithArguments(Integer requestId);
    void deleteUserApiRequestById(Integer requestId);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    UserApiRequest createNewRequest(UserApi userApi, UserApiRequestData userApiRequestData);
    void updateRequestInfo(UserApiRequest userApiRequest, UserApiRequestData userApiRequestData);

    UserApiRequest findUserApiRequestByIdAndApiIdWithArguments(Integer apiId, Integer requestId);
}
