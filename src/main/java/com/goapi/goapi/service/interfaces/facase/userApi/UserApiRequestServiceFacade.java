package com.goapi.goapi.service.interfaces.facase.userApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.goapi.goapi.controller.forms.api.CallApiRequest;
import com.goapi.goapi.controller.forms.api.CreateApiRequestRequest;
import com.goapi.goapi.domain.dto.api.UserApiRequestDto;
import com.goapi.goapi.domain.model.user.User;

import java.util.List;

public interface UserApiRequestServiceFacade {
    UserApiRequestDto createNewUserApiRequest(User user, Integer apiId, CreateApiRequestRequest createApiRequestRequest);

    boolean deleteUserApiRequest(User user, Integer apiId, Integer requestId);

    UserApiRequestDto updateUserApiRequest(User user, Integer apiId, Integer requestId, CreateApiRequestRequest createApiRequestRequest);

    JsonNode doRequest(Integer apiId, Integer requestId, String method, String apiKey, CallApiRequest callApiRequest);

    List<UserApiRequestDto> getUserApiRequests(User user, Integer apiId);

    UserApiRequestDto getUserRequestInfo(User user, Integer apiId, Integer requestId);
}
