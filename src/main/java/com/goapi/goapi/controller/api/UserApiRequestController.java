package com.goapi.goapi.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.goapi.goapi.controller.dto.UserApiRequestDto;
import com.goapi.goapi.controller.form.api.CallApiRequest;
import com.goapi.goapi.controller.form.api.CreateApiRequestRequest;
import com.goapi.goapi.domain.user.User;
import com.goapi.goapi.service.facase.UserApiRequestServiceFacade;
import com.goapi.goapi.service.facase.UserApiServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Daniil Dmitrochenkov
 **/
@RestController
@RequestMapping("/api/request")
@RequiredArgsConstructor
public class UserApiRequestController {

    private final UserApiRequestServiceFacade userApiRequestServiceFacade;

    @PostMapping("/{apiId}")
    public ResponseEntity addRequest(@AuthenticationPrincipal User user, @PathVariable Integer apiId,
                                     @RequestBody CreateApiRequestRequest createApiRequestRequest) {
        UserApiRequestDto newUserApiRequest = userApiRequestServiceFacade.createNewUserApiRequest(user, apiId, createApiRequestRequest);
        return ResponseEntity.ok(newUserApiRequest);
    }

    @DeleteMapping("/{apiId}/{requestId}")
    public ResponseEntity deleteRequest(@AuthenticationPrincipal User user, @PathVariable Integer apiId,
                                        @PathVariable Integer requestId) {
        boolean deleted = userApiRequestServiceFacade.deleteUserApiRequest(user, apiId, requestId);
        ResponseEntity res = null;
        if (deleted) {
            res = ResponseEntity.ok().build();
        } else {
            res = ResponseEntity.badRequest().build();
        }
        return res;
    }

    @PatchMapping("/{apiId}/{requestId}")
    public ResponseEntity updateRequest(@AuthenticationPrincipal User user, @PathVariable Integer apiId,
                                        @PathVariable Integer requestId, @RequestBody CreateApiRequestRequest createApiRequestRequest) {
        UserApiRequestDto updatedUserApiRequest = userApiRequestServiceFacade.updateUserApiRequest(user, apiId, requestId, createApiRequestRequest);
        return ResponseEntity.ok(updatedUserApiRequest);
    }

    @RequestMapping(
        value = "/{apiId}/do/{requestId}",
        method = {
            RequestMethod.GET, RequestMethod.POST,
            RequestMethod.DELETE, RequestMethod.HEAD,
            RequestMethod.PUT, RequestMethod.PATCH,
            RequestMethod.PATCH, RequestMethod.OPTIONS,
            RequestMethod.TRACE
        }
    )
    public ResponseEntity useRequest(@RequestHeader(value = "Authorization", required = false) String apiAuthHeader,
                                     @PathVariable Integer apiId,
                                     @PathVariable Integer requestId,
                                     @RequestBody CallApiRequest callApiRequest) {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String method = req.getMethod();
        String apiKey = apiAuthHeader.substring(7);
        JsonNode res = userApiRequestServiceFacade.doRequest(apiId, requestId, method, apiKey, callApiRequest);
        return ResponseEntity.ok(res);
    }

}
