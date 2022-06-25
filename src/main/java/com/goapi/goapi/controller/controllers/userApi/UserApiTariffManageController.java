package com.goapi.goapi.controller.controllers.userApi;

import com.goapi.goapi.domain.dto.appServiceobject.tariff.UserApiTariffDto;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.service.interfaces.facade.userApi.UserApiTariffServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Daniil Dmitrochenkov
 **/
@RestController
@RequestMapping("/user/api/tariff")
@RequiredArgsConstructor
public class UserApiTariffManageController {

    private final UserApiTariffServiceFacade userApiTariffServiceFacade;

    @GetMapping("/{apiId}")
    public ResponseEntity<UserApiTariffDto> getCurrentUserApiTariff(@AuthenticationPrincipal User user, @PathVariable Integer apiId) {
        UserApiTariffDto userApiTariffDto = userApiTariffServiceFacade.getUserApiTariff(user, apiId);
        return ResponseEntity.ok(userApiTariffDto);
    }

    @PatchMapping("/{apiId}/{tariffId}")
    public ResponseEntity changeUserApiTariff(@AuthenticationPrincipal User user,
                                              @PathVariable Integer tariffId,
                                              @PathVariable Integer apiId) {
        userApiTariffServiceFacade.changeUserApiTariff(user, apiId, tariffId);
        return ResponseEntity.ok().build();
    }

}
