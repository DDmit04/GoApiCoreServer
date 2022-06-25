package com.goapi.goapi.service.interfaces.facade.userApi;

import com.goapi.goapi.controller.forms.userApi.request.CreateApiRequestRequest;
import com.goapi.goapi.controller.forms.userApi.request.UpdateApiRequestRequest;
import com.goapi.goapi.domain.model.appService.userApi.UserApi;

public interface UserApiRequestValidationService {
    void validateRequestDataOnRequestCreate(UserApi userApi, CreateApiRequestRequest createApiRequestRequest);

    void validateRequestDataOnRequestUpdate(UpdateApiRequestRequest updateApiRequestRequest);
}
