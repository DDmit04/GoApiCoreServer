package com.goapi.goapi.service.interfaces.userApi;

import com.goapi.goapi.controller.forms.api.argument.CreateApiRequestArgument;
import com.goapi.goapi.controller.forms.api.argument.UpdateApiRequestArgument;
import com.goapi.goapi.domain.model.userApi.request.UserApiRequest;
import com.goapi.goapi.domain.model.userApi.request.UserApiRequestArgument;

import java.util.List;
import java.util.Set;

public interface UserApiRequestArgumentService {
    List<UserApiRequestArgument> saveRequestArguments(UserApiRequest userApiRequest, List<CreateApiRequestArgument> apiRequestArguments);

    List<UserApiRequestArgument> updateRequestArguments(UserApiRequest userApiRequest, Set<UpdateApiRequestArgument> apiRequestArguments);
}
