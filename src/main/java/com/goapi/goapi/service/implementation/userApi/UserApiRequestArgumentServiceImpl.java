package com.goapi.goapi.service.implementation.userApi;

import com.goapi.goapi.controller.form.api.CreateApiRequestArgument;
import com.goapi.goapi.domain.model.userApi.request.UserApiRequest;
import com.goapi.goapi.domain.model.userApi.request.UserApiRequestArgument;
import com.goapi.goapi.repo.userApi.ApiRequestArgumentRepository;
import com.goapi.goapi.service.interfaces.userApi.UserApiRequestArgumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserApiRequestArgumentServiceImpl implements UserApiRequestArgumentService {

    private final ApiRequestArgumentRepository apiRequestArgumentRepository;

    @Override
    public List<UserApiRequestArgument> saveRequestArguments(UserApiRequest userApiRequest, Set<CreateApiRequestArgument> apiRequestArguments) {
        List<UserApiRequestArgument> userRequestArguments = apiRequestArguments
            .stream()
            .map(req -> new UserApiRequestArgument(req.getArgName(), req.getRequestArgumentType(), userApiRequest))
            .collect(Collectors.toList());
        return apiRequestArgumentRepository.saveAll(userRequestArguments);
    }
}
