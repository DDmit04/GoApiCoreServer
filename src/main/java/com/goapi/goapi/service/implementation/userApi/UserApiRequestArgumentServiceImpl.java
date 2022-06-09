package com.goapi.goapi.service.implementation.userApi;

import com.goapi.goapi.controller.forms.api.argument.CreateApiRequestArgument;
import com.goapi.goapi.controller.forms.api.argument.UpdateApiRequestArgument;
import com.goapi.goapi.domain.model.userApi.request.UserApiRequest;
import com.goapi.goapi.domain.model.userApi.request.UserApiRequestArgument;
import com.goapi.goapi.exception.userApi.requestArgument.UserApiRequestArgumentUpdateException;
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
    public List<UserApiRequestArgument> saveRequestArguments(UserApiRequest userApiRequest, List<CreateApiRequestArgument> apiRequestArguments) {
        List<UserApiRequestArgument> userRequestArguments = apiRequestArguments
            .stream()
            .map(arg -> new UserApiRequestArgument(arg.getArgName(), arg.getRequestArgumentType(), userApiRequest))
            .collect(Collectors.toList());
        return apiRequestArgumentRepository.saveAll(userRequestArguments);
    }

    @Override
    public List<UserApiRequestArgument> updateRequestArguments(UserApiRequest userApiRequest, Set<UpdateApiRequestArgument> updatedApiRequestArguments) {
        Integer requestId = userApiRequest.getId();
        Set<UserApiRequestArgument> currentUserApiRequestArguments = userApiRequest
            .getUserApiRequestArguments();
        List<Integer> updatedArgsIds = updatedApiRequestArguments
            .stream()
            .map(arg -> arg.getId())
            .collect(Collectors.toList());
        List<UserApiRequestArgument> updatedArguments = currentUserApiRequestArguments.stream()
            .map(currentArgument -> {
                Integer currentArgId = currentArgument.getId();
                if (updatedArgsIds.contains(currentArgId)) {
                    UpdateApiRequestArgument updateApiRequestArgument = updatedApiRequestArguments
                        .stream()
                        .filter(updatedArgument -> updatedArgument.getId() == currentArgId)
                        .findFirst()
                        .get();
                    currentArgument.setRequestArgumentType(updateApiRequestArgument.getRequestArgumentType());
                    currentArgument.setArgName(updateApiRequestArgument.getArgName());
                    return currentArgument;
                }
                throw new UserApiRequestArgumentUpdateException(requestId, currentArgId);
            }).collect(Collectors.toList());
        return apiRequestArgumentRepository.saveAll(updatedArguments);
    }
}
