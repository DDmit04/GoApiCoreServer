package com.goapi.goapi.controller.userApi;

import com.goapi.goapi.controller.form.api.CreateApiRequestRequest;
import com.goapi.goapi.domain.dto.api.UserApiRequestDto;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.service.interfaces.facase.UserApiRequestServiceFacade;
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

import java.util.List;

/**
 * @author Daniil Dmitrochenkov
 **/
@RestController
@RequestMapping("/user/api/request")
@RequiredArgsConstructor
public class UserApiRequestController {

    private final UserApiRequestServiceFacade userApiRequestServiceFacade;

    @PostMapping("/{apiId}")
    public ResponseEntity<UserApiRequestDto> addRequest(@AuthenticationPrincipal User user, @PathVariable Integer apiId,
                                     @RequestBody CreateApiRequestRequest createApiRequestRequest) {
        UserApiRequestDto userApiRequest = userApiRequestServiceFacade.createNewUserApiRequest(user, apiId, createApiRequestRequest);
        return ResponseEntity.ok(userApiRequest);
    }

    @GetMapping("/{apiId}")
    public ResponseEntity listApiRequests(@AuthenticationPrincipal User user, @PathVariable Integer apiId) {
        List<UserApiRequestDto> apiRequestList = userApiRequestServiceFacade.getUserApiRequests(user, apiId);
        return ResponseEntity.ok(apiRequestList);
    }

    @GetMapping("/{apiId}/{requestId}")
    public ResponseEntity getRequestInfo(@AuthenticationPrincipal User user, @PathVariable Integer apiId,
                                        @PathVariable Integer requestId) {
        UserApiRequestDto requestDto = userApiRequestServiceFacade.getUserRequestInfo(user, apiId, requestId);
        return ResponseEntity.ok(requestDto);

    }

    @DeleteMapping("/{apiId}/{requestId}")
    public ResponseEntity deleteRequest(@AuthenticationPrincipal User user, @PathVariable Integer apiId,
                                        @PathVariable Integer requestId) {
        boolean deleted = userApiRequestServiceFacade.deleteUserApiRequest(user, apiId, requestId);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{apiId}/{requestId}")
    public ResponseEntity<UserApiRequestDto> updateRequest(@AuthenticationPrincipal User user, @PathVariable Integer apiId,
                                        @PathVariable Integer requestId, @RequestBody CreateApiRequestRequest createApiRequestRequest) {
        UserApiRequestDto updatedUserApiRequest = userApiRequestServiceFacade.updateUserApiRequest(user, apiId, requestId, createApiRequestRequest);
        return ResponseEntity.ok(updatedUserApiRequest);
    }

}
