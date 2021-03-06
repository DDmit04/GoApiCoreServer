package com.goapi.goapi.service.implementation.appService.userApi.request;

import com.goapi.goapi.controller.forms.userApi.request.UserApiRequestData;
import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequest;
import com.goapi.goapi.exception.appService.userApi.request.UserApiRequestNotFoundException;
import com.goapi.goapi.repo.appService.userApi.ApiRequestRepository;
import com.goapi.goapi.service.interfaces.appService.userApi.request.UserApiRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserApiRequestServiceImpl implements UserApiRequestService {

    private final ApiRequestRepository apiRequestRepository;

    @Override
    public UserApiRequest findUserApiRequestByIdWithArguments(Integer requestId) {
        Optional<UserApiRequest> userApiRequestOptional = apiRequestRepository.findByIdWithArguments(requestId);
        return userApiRequestOptional.orElseThrow(() -> new UserApiRequestNotFoundException(requestId));
    }

    @Override
    public void deleteUserApiRequestById(Integer requestId) {
        apiRequestRepository.deleteById(requestId);
    }

    @Override
    public UserApiRequest createNewRequest(UserApi userApi, UserApiRequestData userApiRequestData) {
        HttpMethod httpMethod = userApiRequestData.getHttpMethod();
        String requestTemplate = userApiRequestData.getRequestTemplate();
        String requestName = userApiRequestData.getRequestName();
        UserApiRequest newUserApiRequest = new UserApiRequest(userApi, requestName, requestTemplate, httpMethod);
        return apiRequestRepository.save(newUserApiRequest);
    }

    @Override
    public void updateRequestInfo(UserApiRequest userApiRequest, UserApiRequestData userApiRequestData) {
        HttpMethod httpMethod = userApiRequestData.getHttpMethod();
        String requestTemplate = userApiRequestData.getRequestTemplate();
        String requestName = userApiRequestData.getRequestName();
        userApiRequest.setHttpMethod(httpMethod);
        userApiRequest.setRequestName(requestName);
        userApiRequest.setRequestTemplate(requestTemplate);
        apiRequestRepository.save(userApiRequest);
    }

    @Override
    public UserApiRequest findUserApiRequestByIdAndApiIdWithArguments(Integer apiId, Integer requestId) {
        Optional<UserApiRequest> userApiRequestOptional = apiRequestRepository.findByIdAndUserApiIdWithArguments(requestId, apiId);
        return userApiRequestOptional.orElseThrow(() -> new UserApiRequestNotFoundException(requestId));
    }

}
