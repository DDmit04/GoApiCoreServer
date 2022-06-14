package com.goapi.goapi.service.interfaces.appService.userApi;

import com.goapi.goapi.controller.forms.userApi.argument.UpdateApiRequestArgument;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequest;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequestArgument;

import java.util.List;
import java.util.Set;

public interface UserApiRequestArgumentService {

    List<UserApiRequestArgument> updateRequestArguments(UserApiRequest userApiRequest, Set<UpdateApiRequestArgument> apiRequestArguments);
}
