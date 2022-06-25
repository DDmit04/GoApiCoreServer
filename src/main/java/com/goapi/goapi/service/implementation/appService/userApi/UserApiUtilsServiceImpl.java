package com.goapi.goapi.service.implementation.appService.userApi;

import com.goapi.goapi.UrlUtils;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequest;
import com.goapi.goapi.service.implementation.appService.userApi.query.builder.QueryRequestBuilder;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiUtilsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

    @Override
    public String buildUserApiRequestQuery(UserApiRequest userApiRequest, Map<String, Object> arguments) {
        QueryRequestBuilder structure = new QueryRequestBuilder(userApiRequest);
        arguments.entrySet()
            .stream()
            .forEach(entry -> {
                String argName = entry.getKey();
                Object argValue = entry.getValue();
                structure.setArgument(argName, argValue);
            });
        String query = structure.getQuery();
        return query;
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
