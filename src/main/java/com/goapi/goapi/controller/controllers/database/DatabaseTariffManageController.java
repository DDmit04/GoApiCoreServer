package com.goapi.goapi.controller.controllers.database;

import com.goapi.goapi.domain.dto.appServiceobject.tariff.DatabaseTariffDto;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.service.interfaces.facade.database.DatabaseTariffServiceFacade;
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
@RequestMapping("/user/db/tariff")
@RequiredArgsConstructor
public class DatabaseTariffManageController {

    private final DatabaseTariffServiceFacade databaseTariffServiceFacade;

    @GetMapping("/{dbId}")
    public ResponseEntity<DatabaseTariffDto> getDatabaseTariff(@AuthenticationPrincipal User user, @PathVariable Integer dbId) {
        DatabaseTariffDto databaseTariffDto = databaseTariffServiceFacade.getDatabaseTariff(user, dbId);
        return ResponseEntity.ok(databaseTariffDto);
    }

    @PatchMapping("/{dbId}/{tariffId}")
    public ResponseEntity changeDatabaseTariff(@AuthenticationPrincipal User user, @PathVariable Integer dbId, @PathVariable Integer tariffId) {
        databaseTariffServiceFacade.changeDatabaseTariff(user, dbId, tariffId);
        return ResponseEntity.ok().build();
    }
}
