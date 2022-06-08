package com.goapi.goapi.controller.controllers.userApi;

import com.goapi.goapi.domain.dto.tariff.UserApiTariffDto;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.service.interfaces.facase.userApi.UserApiTariffServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping
    public ResponseEntity<UserApiTariffDto> getCurrentUserApiTariff(@AuthenticationPrincipal User user) {
        UserApiTariffDto userApiTariffDto = userApiTariffServiceFacade.getUserApiTariff(user);
        return ResponseEntity.ok(userApiTariffDto);
    }
    @PostMapping("/{tariffId}")
    public ResponseEntity chooseUserApiTariff(@AuthenticationPrincipal User user, @PathVariable Integer tariffId) {
        userApiTariffServiceFacade.setUserApiTariff(user, tariffId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{tariffId}")
    public ResponseEntity changeUserApiTariff(@AuthenticationPrincipal User user, @PathVariable Integer tariffId) {
        boolean changed = userApiTariffServiceFacade.changeUserApiTariff(user, tariffId);
        if(changed) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
