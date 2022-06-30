package com.goapi.goapi.service.implementation.appService.userApi.request;

import com.goapi.goapi.controller.forms.userApi.argument.UpdateApiRequestArgument;
import com.goapi.goapi.domain.model.appService.userApi.request.RequestArgumentType;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequest;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequestArgument;
import com.goapi.goapi.repo.appService.userApi.ApiRequestArgumentRepository;
import com.goapi.goapi.service.interfaces.appService.userApi.request.UserApiRequestArgumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
    public List<UserApiRequestArgument> updateRequestArguments(UserApiRequest userApiRequest, Set<UpdateApiRequestArgument> updatedApiRequestArguments) {
        Set<UserApiRequestArgument> currentUserApiRequestArguments = userApiRequest.getUserApiRequestArguments();
        Set<UserApiRequestArgument> updatedArguments = getUpdatedUserApiRequestArguments(updatedApiRequestArguments, currentUserApiRequestArguments);
        Set<UserApiRequestArgument> newArguments = getNewUserApiRequestArguments(userApiRequest, updatedApiRequestArguments);
        HashSet<UserApiRequestArgument> argumentsToSave = new HashSet<>() {{
            addAll(newArguments);
            addAll(updatedArguments);
        }};
        return apiRequestArgumentRepository.saveAll(argumentsToSave);
    }

    private Set<UserApiRequestArgument> getNewUserApiRequestArguments(UserApiRequest userApiRequest, Set<UpdateApiRequestArgument> updatedApiRequestArguments) {
        Set<UserApiRequestArgument> newArguments = updatedApiRequestArguments
            .stream()
            .filter(newArg -> newArg.getId() == null)
            .map(newArg -> {
                String argName = newArg.getArgName();
                RequestArgumentType requestArgumentType = newArg.getRequestArgumentType();
                return new UserApiRequestArgument(argName, requestArgumentType, userApiRequest);
            })
            .collect(Collectors.toSet());
        return newArguments;
    }

    private Set<UserApiRequestArgument> getUpdatedUserApiRequestArguments(Set<UpdateApiRequestArgument> updatedApiRequestArguments, Set<UserApiRequestArgument> currentUserApiRequestArguments) {
        Set<UserApiRequestArgument> updatedArguments = currentUserApiRequestArguments
            .stream()
            .filter(currentArgument -> {
                Optional<UpdateApiRequestArgument> argumentUpdateData = findUpdateArgumentData(updatedApiRequestArguments, currentArgument);
                return argumentUpdateData
                    .map(argUpdateData -> {
                        updateUserApiRequestArgument(currentArgument, argUpdateData);
                        return true;
                    })
                    .orElse(false);
            }).collect(Collectors.toSet());
        return updatedArguments;
    }

    private void updateUserApiRequestArgument(UserApiRequestArgument currentArgument, UpdateApiRequestArgument argUpdateData) {
        RequestArgumentType newArgumentType = argUpdateData.getRequestArgumentType();
        String newArgName = argUpdateData.getArgName();
        currentArgument.setRequestArgumentType(newArgumentType);
        currentArgument.setArgName(newArgName);
    }

    private Optional<UpdateApiRequestArgument> findUpdateArgumentData(Set<UpdateApiRequestArgument> updatedApiRequestArguments, UserApiRequestArgument currentArgument) {
        Optional<UpdateApiRequestArgument> argumentUpdateData = updatedApiRequestArguments
            .stream()
            .filter(arg -> {
                Integer updatedArgId = arg.getId();
                Integer currentArgumentId = currentArgument.getId();
                boolean argsAreEqual = arg.equals(currentArgument);
                return updatedArgId != null
                    && updatedArgId.equals(currentArgumentId)
                    && !argsAreEqual;
            })
            .findFirst();
        return argumentUpdateData;
    }
}
