package com.goapi.goapi.controller.database;

import com.goapi.goapi.domain.dto.tariff.DatabaseTariffDto;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.service.interfaces.facase.DatabaseTariffServiceFacade;
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
        boolean changed = databaseTariffServiceFacade.changeDatabaseTariff(user, dbId, tariffId);
        if (changed) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
