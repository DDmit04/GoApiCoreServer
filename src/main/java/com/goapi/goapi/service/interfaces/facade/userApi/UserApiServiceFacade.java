package com.goapi.goapi.service.interfaces.facade.userApi;

import com.goapi.goapi.controller.forms.userApi.CreateUserApiRequest;
import com.goapi.goapi.domain.dto.appServiceobject.userApi.SummaryUserApiDto;
import com.goapi.goapi.domain.dto.appServiceobject.userApi.UserApiDto;
import com.goapi.goapi.domain.model.user.User;

import java.util.List;

public interface UserApiServiceFacade {
    SummaryUserApiDto createApi(User user, CreateUserApiRequest createUserApiRequest);

    boolean deleteApi(User user, Integer apiId);

    String getUserApiKey(User user, Integer apiId);

    boolean refreshApiKey(User user, Integer apiId);

    List<SummaryUserApiDto> getUserApisInfo(User user);

    void renameUserApi(User user, Integer apiId, String newUserApiName);

    UserApiDto getUserApiInfo(User user, Integer apiId);

}
