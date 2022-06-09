package com.goapi.goapi.controller.controllers.userApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.goapi.goapi.controller.forms.api.CallApiRequest;
import com.goapi.goapi.service.interfaces.facase.userApi.UserApiRequestServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Daniil Dmitrochenkov
 **/
@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserApiController {

    private final UserApiRequestServiceFacade userApiRequestServiceFacade;

    @Value("${urls.api-request.path.param-name.apiId}")
    private String userApiIdPathParamName;

    @Value("${urls.api-request.path.param-name.requestId}")
    private String requestIdPathParamName;

    @RequestMapping(
        value = "${urls.api-request.path.start}",
        method = {
            RequestMethod.GET, RequestMethod.POST,
            RequestMethod.DELETE, RequestMethod.HEAD,
            RequestMethod.PUT, RequestMethod.PATCH,
            RequestMethod.PATCH, RequestMethod.OPTIONS,
            RequestMethod.TRACE
        }
    )
    public ResponseEntity useRequest(@RequestHeader(value = "Authorization", required = false) String apiAuthHeader,
                                     @RequestParam Map<String, String> queryParams,
                                     @RequestBody CallApiRequest callApiRequest) {
        Integer apiId = Integer.valueOf(queryParams.get(userApiIdPathParamName));
        Integer requestId = Integer.valueOf(queryParams.get(requestIdPathParamName));
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String method = req.getMethod();
        String apiKey = extractUserApiKey(apiAuthHeader);
        JsonNode res = userApiRequestServiceFacade.doUserApiRequest(apiId, requestId, method, apiKey, callApiRequest);
        return ResponseEntity.ok(res);
    }

    private String extractUserApiKey(String authHeader) {
        String apiKey = authHeader.substring(7);
        return apiKey;
    }
}
