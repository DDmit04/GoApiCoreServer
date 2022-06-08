package com.goapi.goapi.service.interfaces.userApi;

import com.goapi.goapi.controller.forms.api.CreateApiRequestArgument;
import com.goapi.goapi.domain.model.userApi.request.UserApiRequest;
import com.goapi.goapi.domain.model.userApi.request.UserApiRequestArgument;

import java.util.List;
import java.util.Set;

public interface UserApiRequestArgumentService {
    List<UserApiRequestArgument> saveRequestArguments(UserApiRequest userApiRequest, Set<CreateApiRequestArgument> apiRequestArguments);
}
