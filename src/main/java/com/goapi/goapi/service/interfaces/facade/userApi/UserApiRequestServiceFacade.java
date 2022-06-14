package com.goapi.goapi.service.interfaces.facade.userApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.goapi.goapi.controller.forms.userApi.CallApiRequest;
import com.goapi.goapi.controller.forms.userApi.request.CreateApiRequestRequest;
import com.goapi.goapi.controller.forms.userApi.request.UpdateApiRequestRequest;
import com.goapi.goapi.domain.dto.userApi.UserApiRequestDto;
import com.goapi.goapi.domain.model.user.User;

import java.util.List;

public interface UserApiRequestServiceFacade {
    UserApiRequestDto createUserApiRequest(User user, Integer apiId, CreateApiRequestRequest createApiRequestRequest);

    boolean deleteUserApiRequest(User user, Integer apiId, Integer requestId);

    UserApiRequestDto updateUserApiRequest(User user, Integer apiId, UpdateApiRequestRequest updateApiRequestRequest);

    JsonNode doUserApiRequest(Integer apiId, Integer requestId, String method, String apiKey, CallApiRequest callApiRequest);

    List<UserApiRequestDto> getUserApiRequests(User user, Integer apiId);

    UserApiRequestDto getUserApiRequestInfo(User user, Integer apiId, Integer requestId);
}
