package com.goapi.goapi.service.api;

import com.goapi.goapi.domain.api.UserApi;
import com.goapi.goapi.domain.api.request.RequestArgumentType;
import com.goapi.goapi.domain.api.request.UserApiRequest;
import com.goapi.goapi.domain.api.request.UserApiRequestArgument;
import com.goapi.goapi.repo.api.ApiRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.management.ObjectName;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserApiRequestService {

    private final ApiRequestRepository apiRequestRepository;

    public Optional<UserApiRequest> findUserApiRequestById(Integer requestId) {
        return apiRequestRepository.findById(requestId);
    }

    public void deleteUserApiRequestById(Integer requestId) {
        apiRequestRepository.deleteById(requestId);
    }

    public UserApiRequest createNewRequest(UserApi userApi, String requestName, String requestTemplate, HttpMethod httpMethod) {
        UserApiRequest newUserApiRequest = new UserApiRequest(userApi, requestName, requestTemplate, httpMethod);
        return apiRequestRepository.save(newUserApiRequest);
    }

    public String buildRequestQuery(UserApiRequest userApiRequest, Map<String, Object> arguments) {
        StringBuilder templateBuilder = new StringBuilder(userApiRequest.getRequestTemplate());
        arguments.entrySet().forEach(arg -> {
            String argName = arg.getKey();
            Object argValue = arg.getValue();
            Optional<UserApiRequestArgument> matchingArgument = userApiRequest.getUserApiRequestArguments()
                .stream()
                .filter(ar -> ar.getArgName().equals(argName))
                .findFirst();
            //TODO fix if arg value == arg name (${val} = '${name}')
            matchingArgument.map(matchArg -> {
                RequestArgumentType requestArgumentType = matchArg.getRequestArgumentType();
                return placeArgInTemplate(templateBuilder, argName, argValue, requestArgumentType);
            }).orElseThrow(() -> new RuntimeException());
        });

        templateBuilder.append(";");
        return templateBuilder.toString();
    }

    @SneakyThrows
    private StringBuilder placeArgInTemplate(StringBuilder templateBuilder, String argName, Object argValue, RequestArgumentType requestArgumentType) {
        String argReplace = "${" + argName + "}";
        String argReplacement = "";
        //TODO SECURITY
        switch (requestArgumentType) {
            case INTEGER -> argReplacement = String.format("CAST('%s' as int)", argValue.toString());
            case STRING -> argReplacement = String.format("CAST('%s' as varchar)", argValue.toString());
            case FLOAT -> argReplacement = String.format("CAST('%s' as float)", argValue.toString());
            case DATE -> argReplacement = String.format("CAST('%s' as date)", argValue.toString());
            case DATETIME -> argReplacement = String.format("CAST('%s' as datetime)", argValue.toString());
            case IDENTIFIER -> argReplacement = argValue.toString();
        }

        int start = templateBuilder.indexOf(argReplace);
        int length = argReplace.length();
        return templateBuilder.replace(start, start + length, argReplacement);
    }
}
