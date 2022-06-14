package com.goapi.goapi.controller.controllers.userApi;

import com.goapi.goapi.domain.dto.tariff.UserApiTariffDto;
import com.goapi.goapi.service.interfaces.appService.userApi.UserApiTariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Daniil Dmitrochenkov
 **/
@RestController
@RequestMapping("/api/tariff")
@RequiredArgsConstructor
public class UserApiTariffController {

    private final UserApiTariffService userApiTariffService;

    @GetMapping
    public ResponseEntity<List<UserApiTariffDto>> getTariffs() {
        List<UserApiTariffDto> userApiTariffDtoList = userApiTariffService.listUserApiTariffs();
        return ResponseEntity.ok(userApiTariffDtoList);
    }

}
