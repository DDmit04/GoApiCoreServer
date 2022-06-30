package com.goapi.goapi.service.implementation.appService.userApi;

import com.goapi.goapi.UrlUtils;
import com.goapi.goapi.domain.model.appService.userApi.request.RequestArgumentType;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequest;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequestArgument;
import com.goapi.goapi.exception.appService.userApi.requestArgument.UserApiRequestArgumentMismatchException;
import com.goapi.goapi.service.implementation.appService.userApi.query.argReplaceSuplier.TemplateArgumentReplaceSupplier;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiUtilsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserApiUtilsServiceImpl implements UserApiUtilsService {

    @Value("${urls.api-request.path.start}")
    private String doRequestUrl;
    @Value("${urls.api-request.path.param-name.apiId}")
    private String apiIdParamName;
    @Value("${urls.api-request.path.param-name.requestId}")
    private String requestIdParamName;

    @Value("${string.request-arg-regex}")
    private String requestArgRegex;
    private Pattern templateArgPattern;

    @PostConstruct
    public void postConstruct() {
        this.templateArgPattern = Pattern.compile(requestArgRegex);
    }

    @Override
    public String buildUserApiRequestQuery(UserApiRequest userApiRequest, Map<String, Object> arguments) {

        Map<String, String> resArguments = new HashMap<>();
        String template = userApiRequest.getRequestTemplate();
        Matcher matcher = templateArgPattern.matcher(template);
        List<String> templateArgNames = new ArrayList<>();
        while (matcher.find()) {
            templateArgNames.add(matcher.group(1));
        }
        arguments.entrySet()
            .stream()
            .forEach(entry -> {
                String argName = entry.getKey();
                if (templateArgNames.contains(argName)) {
                    RequestArgumentType argumentType = getArgumentTypeByName(userApiRequest, argName);
                    TemplateArgumentReplaceSupplier argumentReplaceSupplier = argumentType.getSupplier();
                    Object argValue = entry.getValue();
                    String argStringValue = argumentReplaceSupplier.getTemplateArgumentReplacement(argValue);
                    resArguments.put(argName, argStringValue);
                }
            });

        String resultQuery = addPathParamsToQueryTemplate(template, resArguments);
        return resultQuery;

//        QueryRequestBuilder structure = new QueryRequestBuilder(userApiRequest);
//        arguments.entrySet()
//            .stream()
//            .forEach(entry -> {
//                String argName = entry.getKey();
//                Object argValue = entry.getValue();
//                structure.setArgument(argName, argValue);
//            });
//        String query = structure.getQuery();
//        return query;
    }

    private RequestArgumentType getArgumentTypeByName(UserApiRequest userApiRequest, String argName) {
        Optional<UserApiRequestArgument> argumentOptional = userApiRequest
            .getUserApiRequestArguments()
            .stream()
            .filter(ar -> ar.getArgName().equals(argName))
            .findFirst();
        return argumentOptional.map(arg -> {
            RequestArgumentType argumentType = arg.getRequestArgumentType();
            return argumentType;
        }).orElseThrow(() -> new UserApiRequestArgumentMismatchException(argName));
    }

    public String addPathParamsToQueryTemplate(String template, Map<String, String> pathParams) {
        String finalUrl = UriComponentsBuilder
            .fromUriString(template)
            .buildAndExpand(pathParams)
            .toString();
        return finalUrl;
    }

    @Override
    public String getUserApiRequestUrl(UserApiRequest userApiRequest) {
        Integer userApiId = userApiRequest.getUserApi().getId();
        Integer requestId = userApiRequest.getId();
        Map<String, String> params = new HashMap<>() {{
            put(apiIdParamName, userApiId.toString());
            put(requestIdParamName, requestId.toString());
        }};
        String finalUrl = UrlUtils.addQueryParamsToUrl(doRequestUrl, params);
        return finalUrl;
    }

}
