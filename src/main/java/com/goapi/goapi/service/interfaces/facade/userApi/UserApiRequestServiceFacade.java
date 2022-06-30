package com.goapi.goapi.service.interfaces.facade.userApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.goapi.goapi.controller.forms.userApi.CallApiRequest;
import com.goapi.goapi.controller.forms.userApi.request.UpdateApiRequestRequest;
import com.goapi.goapi.controller.forms.userApi.request.UserApiRequestData;
import com.goapi.goapi.domain.dto.appServiceobject.userApi.UserApiRequestDto;
import com.goapi.goapi.domain.dto.appServiceobject.userApi.summary.SummaryUserApiRequestDto;
import com.goapi.goapi.domain.model.user.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserApiRequestServiceFacade {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    SummaryUserApiRequestDto createUserApiRequest(User user, Integer apiId, UserApiRequestData createApiRequestRequest);

    boolean deleteUserApiRequest(User user, Integer apiId, Integer requestId);

    SummaryUserApiRequestDto updateUserApiRequest(User user, Integer apiId, UpdateApiRequestRequest updateApiRequestRequest);

    JsonNode doUserApiRequest(Integer apiId, Integer requestId, String method, String apiKey, CallApiRequest callApiRequest);

    List<SummaryUserApiRequestDto> getUserApiRequests(User user, Integer apiId);

    UserApiRequestDto getUserApiRequestInfo(User user, Integer apiId, Integer requestId);
}
