package com.goapi.goapi.controller.controllers.userApi;

import com.goapi.goapi.controller.forms.RenameForm;
import com.goapi.goapi.controller.forms.userApi.CreateUserApiRequest;
import com.goapi.goapi.domain.dto.appServiceobject.database.DatabaseDto;
import com.goapi.goapi.domain.dto.appServiceobject.userApi.SummaryUserApiDto;
import com.goapi.goapi.domain.dto.appServiceobject.userApi.UserApiDto;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.service.interfaces.facade.userApi.UserApiServiceFacade;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
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

import javax.validation.Valid;
import java.util.List;

/**
 * @author Daniil Dmitrochenkov
 **/
@RestController
@RequestMapping("/user/api")
@RequiredArgsConstructor
public class UserApiManageController {

    private final UserApiServiceFacade userApiServiceFacade;


    @PostMapping
    public ResponseEntity createUserApi(@AuthenticationPrincipal User user, @RequestBody CreateUserApiRequest createUserApiRequest) {
        SummaryUserApiDto apiDto = userApiServiceFacade.createApi(user, createUserApiRequest);
        return ResponseEntity.ok(apiDto);
    }

    @GetMapping("/{apiId}")
    public ResponseEntity<UserApiDto> getUserApiInfo(@AuthenticationPrincipal User user, @PathVariable Integer apiId) {
        UserApiDto userApiDto = userApiServiceFacade.getUserApiInfo(user, apiId);
        return ResponseEntity.ok(userApiDto);
    }

    @DeleteMapping("/{apiId}")
    public ResponseEntity deleteUserApi(@AuthenticationPrincipal User user, @PathVariable Integer apiId) {
        boolean deleted = userApiServiceFacade.deleteApi(user, apiId);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<SummaryUserApiDto>> getUserApisInfo(@AuthenticationPrincipal User user) {
        List<SummaryUserApiDto> userApisInfoDto = userApiServiceFacade.getUserApisInfo(user);
        return ResponseEntity.ok(userApisInfoDto);
    }

    @PatchMapping("/{apiId}")
    public ResponseEntity<DatabaseDto> renameUserApi(@AuthenticationPrincipal User user, @Valid @RequestBody RenameForm renameForm, @PathVariable Integer apiId) {
        String newUserApiName = renameForm.getName();
        userApiServiceFacade.renameUserApi(user, apiId, newUserApiName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/key/{apiId}")
    public ResponseEntity getApiKey(@AuthenticationPrincipal User user, @PathVariable Integer apiId) {
        String key = userApiServiceFacade.getUserApiKey(user, apiId);
        JSONObject resp = new JSONObject();
        resp.put("key", key);
        return ResponseEntity.ok(resp);
    }

    @PatchMapping("/key/{apiId}")
    public ResponseEntity refreshApiKey(@AuthenticationPrincipal User user, @PathVariable Integer apiId) {
        boolean refreshed = userApiServiceFacade.refreshApiKey(user, apiId);
        if (refreshed) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
