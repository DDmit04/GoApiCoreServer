package com.goapi.goapi.service.interfaces.facase;

import com.goapi.goapi.controller.form.api.CreateUserApiRequest;
import com.goapi.goapi.domain.dto.api.SummaryUserApiDto;
import com.goapi.goapi.domain.dto.api.UserApiDto;
import com.goapi.goapi.domain.model.user.User;

import java.util.List;

public interface UserApiServiceFacade {
    SummaryUserApiDto createApi(User user, CreateUserApiRequest createUserApiRequest);

    boolean deleteApi(User user, Integer apiId);

    String getUserApiKey(User user, Integer apiId);

    boolean refreshApiKey(User user, Integer apiId);

    List<SummaryUserApiDto> listUserApis(User user);

    boolean renameUserApi(User user, Integer apiId, String newUserApiName);

    UserApiDto getUserApiInfo(User user, Integer apiId);
}
