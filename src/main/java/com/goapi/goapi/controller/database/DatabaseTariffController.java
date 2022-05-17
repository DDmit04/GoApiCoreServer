package com.goapi.goapi.controller.database;

import com.fasterxml.jackson.annotation.JsonView;
import com.goapi.goapi.domain.database.DatabaseTariff;
import com.goapi.goapi.service.database.DatabaseTariffService;
import com.goapi.goapi.views.CommonView;
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
@RequestMapping("/tariff")
@RequiredArgsConstructor
public class DatabaseTariffController {
    private final DatabaseTariffService dbTariffService;

    @GetMapping
    @JsonView(CommonView.CoreData.class)
    public ResponseEntity<List<DatabaseTariff>> getTariffs() {
        return ResponseEntity.ok(dbTariffService.listTariffs());
    }
}
