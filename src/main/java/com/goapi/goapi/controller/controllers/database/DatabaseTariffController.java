package com.goapi.goapi.controller.controllers.database;

import com.goapi.goapi.domain.dto.appServiceobject.tariff.DatabaseTariffDto;
import com.goapi.goapi.service.interfaces.appService.database.DatabaseTariffService;
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
@RequestMapping("/db/tariff")
@RequiredArgsConstructor
public class DatabaseTariffController {
    private final DatabaseTariffService dbTariffService;

    @GetMapping
    public ResponseEntity<List<DatabaseTariffDto>> getTariffs() {
        List<DatabaseTariffDto> databaseTariffDtoList = dbTariffService.listTariffs();
        return ResponseEntity.ok(databaseTariffDtoList);
    }
}
