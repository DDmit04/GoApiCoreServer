package com.goapi.goapi.service.interfaces.appService.userApi.request;

import com.goapi.goapi.controller.forms.userApi.argument.UpdateApiRequestArgument;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequest;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequestArgument;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional
public interface UserApiRequestArgumentService {

    List<UserApiRequestArgument> updateRequestArguments(UserApiRequest userApiRequest, Set<UpdateApiRequestArgument> apiRequestArguments);
}
