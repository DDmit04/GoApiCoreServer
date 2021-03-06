package com.goapi.goapi.service.implementation.facade.appService.userApi;

import com.goapi.goapi.controller.forms.userApi.argument.UpdateApiRequestArgument;
import com.goapi.goapi.controller.forms.userApi.argument.UserApiRequestArgumentData;
import com.goapi.goapi.controller.forms.userApi.request.UpdateApiRequestRequest;
import com.goapi.goapi.controller.forms.userApi.request.UserApiRequestData;
import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import com.goapi.goapi.exception.appService.userApi.request.UserApiRequestArgNotUsedTemplateException;
import com.goapi.goapi.exception.appService.userApi.request.UserApiRequestsCountCupException;
import com.goapi.goapi.exception.appService.userApi.requestArgument.UserApiRequestArgumentInvalidNameException;
import com.goapi.goapi.exception.appService.userApi.requestArgument.UserApiRequestArgumentsNotUniqueException;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiService;
import com.goapi.goapi.service.interfaces.facade.userApi.UserApiRequestValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserApiRequestValidationServiceImpl implements UserApiRequestValidationService {

    private final UserApiService userApiService;

    @Value("${string.request-arg-pattern}")
    private String requestArgPattern;

    @Override
    public void validateRequestDataOnRequestCreate(UserApi userApi, UserApiRequestData userApiRequestData) {
        validateTotalRequestsCount(userApi);
    }

    @Override
    public void validateRequestDataOnRequestUpdate(UpdateApiRequestRequest updateApiRequestRequest) {
        Set<UpdateApiRequestArgument> arguments = updateApiRequestRequest.getApiRequestArguments();
        String requestTemplate = updateApiRequestRequest.getRequestTemplate();
        validateRequestTemplate(requestTemplate, arguments);
    }
    private void validateRequestTemplate(String requestTemplate, Collection<? extends UserApiRequestArgumentData> arguments) {
        validateRequestArgumentsNamesLength(arguments);
        validateRequestArgumentsUniqueNames(arguments);
        validateRequestTemplateArguments(requestTemplate, arguments);
    }

    private void validateRequestArgumentsUniqueNames(Collection<? extends UserApiRequestArgumentData> arguments) {
        List<String> uniqueArgsNames = arguments.stream()
            .map(arg -> arg.getArgName())
            .distinct()
            .collect(Collectors.toList());
        int uniqueArgsCount = uniqueArgsNames.size();
        if (uniqueArgsCount != arguments.size()) {
            arguments.removeAll(uniqueArgsNames);
            List<String> duplicateArgsNames = arguments.stream()
                .map(arg -> arg.getArgName())
                .distinct()
                .collect(Collectors.toList());
            throw new UserApiRequestArgumentsNotUniqueException(duplicateArgsNames);
        }
    }

    private void validateRequestArgumentsNamesLength(Collection<? extends UserApiRequestArgumentData> arguments) {
        Optional<? extends UserApiRequestArgumentData> zeroLengthNamesArgs = arguments
            .stream()
            .filter(arg -> !StringUtils.hasLength(arg.getArgName()))
            .findAny();
        if (zeroLengthNamesArgs.isPresent()) {
            throw new UserApiRequestArgumentInvalidNameException();
        }
    }

    private void validateRequestTemplateArguments(String template, Collection<? extends UserApiRequestArgumentData> arguments) {
        Set<String> argumentNames = arguments.stream()
            .map(arg -> arg.getArgName())
            .collect(Collectors.toSet());
        argumentNames.forEach(argName -> {
            int argUsageIndex = template.indexOf(String.format(requestArgPattern, argName));
            if (argUsageIndex == -1) {
                throw new UserApiRequestArgNotUsedTemplateException(argName);
            }
        });
    }

    private void validateTotalRequestsCount(UserApi userApi) {
        Integer userApiId = userApi.getId();
        Integer tariffMaxRequestsCount = userApi.getAppServiceTariff().getMaxRequestsCount();
        int totalUserRequestsCount = userApiService.getUserApiRequestsCountById(userApiId);
        boolean lowerThanMaxApisCountTariff = totalUserRequestsCount < tariffMaxRequestsCount;
        if (!lowerThanMaxApisCountTariff) {
            throw new UserApiRequestsCountCupException(userApiId);
        }
    }

}
