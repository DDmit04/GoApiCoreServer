package com.goapi.goapi.service.api;

import com.goapi.goapi.controller.form.api.CreateApiRequestArgument;
import com.goapi.goapi.domain.api.UserApi;
import com.goapi.goapi.domain.api.request.UserApiRequest;
import com.goapi.goapi.domain.api.request.UserApiRequestArgument;
import com.goapi.goapi.repo.api.ApiRequestArgumentRepository;
import com.goapi.goapi.repo.api.ApiRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserApiRequestArgumentService {

    private final ApiRequestArgumentRepository apiRequestArgumentRepository;

    public List<UserApiRequestArgument> saveRequestArguments(UserApiRequest userApiRequest, Set<CreateApiRequestArgument> apiRequestArguments) {
        List<UserApiRequestArgument> userRequestArguments = apiRequestArguments
            .stream()
            .map(req -> new UserApiRequestArgument(req.getArgName(), req.getRequestArgumentType(), userApiRequest))
            .collect(Collectors.toList());
        return apiRequestArgumentRepository.saveAll(userRequestArguments);
    }
}
