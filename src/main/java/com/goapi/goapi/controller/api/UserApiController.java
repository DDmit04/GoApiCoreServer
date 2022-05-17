package com.goapi.goapi.controller.api;

import com.goapi.goapi.controller.form.api.CreateUserApiRequest;
import com.goapi.goapi.controller.form.api.UserApiShortData;
import com.goapi.goapi.domain.user.User;
import com.goapi.goapi.service.facase.UserApiRequestServiceFacade;
import com.goapi.goapi.service.facase.UserApiServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author Daniil Dmitrochenkov
 **/
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApiController {

    private final UserApiServiceFacade userApiServiceFacade;

    @PostMapping
    public ResponseEntity createUserApi(@AuthenticationPrincipal User user, @RequestBody CreateUserApiRequest createUserApiRequest) {
        UserApiShortData apiData = userApiServiceFacade.createApi(user, createUserApiRequest);
        return ResponseEntity.ok(apiData);
    }

    @DeleteMapping("/{apiId}")
    public ResponseEntity deleteUserApi(@AuthenticationPrincipal User user, @PathVariable Integer apiId) {
        boolean deleted = userApiServiceFacade.deleteApi(user, apiId);
        ResponseEntity res = null;
        if (deleted) {
            res = ResponseEntity.ok().build();
        } else {
            res = ResponseEntity.badRequest().build();
        }
        return res;
    }

    @GetMapping("/{apiId}")
    public ResponseEntity getApiKey(@AuthenticationPrincipal User user, @PathVariable Integer apiId) {
        String key = userApiServiceFacade.getUserApiKey(user, apiId);
        return ResponseEntity.ok(new HashMap<String, String>() {{
            put("key", key);
        }});
    }

    @PatchMapping("/{apiId}")
    public ResponseEntity refreshApiKey(@AuthenticationPrincipal User user, @PathVariable Integer apiId) {
        boolean refreshed = userApiServiceFacade.refreshApiKey(user, apiId);
        ResponseEntity res = null;
        if (refreshed) {
            res = ResponseEntity.ok().build();
        } else {
            res = ResponseEntity.badRequest().build();
        }
        return res;
    }

}
