package com.goapi.goapi.controller.database;

import com.fasterxml.jackson.annotation.JsonView;
import com.goapi.goapi.controller.dto.DatabaseAuthInfo;
import com.goapi.goapi.controller.form.database.ChangeDatabaseTariffForm;
import com.goapi.goapi.controller.form.database.CreateDatabaseForm;
import com.goapi.goapi.controller.dto.DatabaseInfoDTO;
import com.goapi.goapi.controller.form.database.DatabasePasswordForm;
import com.goapi.goapi.domain.database.Database;
import com.goapi.goapi.domain.database.DatabaseTariff;
import com.goapi.goapi.domain.user.User;
import com.goapi.goapi.service.database.DatabaseService;
import com.goapi.goapi.service.facase.DatabaseServiceFacade;
import com.goapi.goapi.views.CommonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
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
import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 **/
@RestController
@RequestMapping("/db")
@RequiredArgsConstructor
public class DatabaseController {

    private final DatabaseServiceFacade databaseServiceFacade;
    private final DatabaseService databaseService;

    @PostMapping
    public ResponseEntity<DatabaseAuthInfo> createNewDatabase(@AuthenticationPrincipal User user, @Valid @RequestBody CreateDatabaseForm createDbForm) {
        Optional<DatabaseAuthInfo> dbInfoOptional = databaseServiceFacade.createNewDatabase(user, createDbForm);
        return dbInfoOptional.map(info -> ResponseEntity.ok(info))
            .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping
    @JsonView(CommonView.CoreData.class)
    public ResponseEntity<List<Database>> listDatabases(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(databaseService.listUserDatabases(user));
    }

    @GetMapping("/{dbId}")
    public ResponseEntity<DatabaseInfoDTO> getDatabaseInfo(@AuthenticationPrincipal User user, @PathVariable Integer dbId) {
        return ResponseEntity.ok(databaseServiceFacade.getDatabaseInfo(user, dbId));
    }

    @PatchMapping("/{dbId}")
    public ResponseEntity generateNewDbPassword(@AuthenticationPrincipal User user, @PathVariable Integer dbId) {
        boolean updated = databaseServiceFacade.generateNewDbPassword(user, dbId);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{dbId}")
    public ResponseEntity deleteDatabase(@AuthenticationPrincipal User user, @PathVariable Integer dbId) {
        boolean deleted;
        deleted = databaseServiceFacade.deleteDatabase(user, dbId);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{dbId}/password")
    public ResponseEntity<DatabasePasswordForm> getDbPassword(@AuthenticationPrincipal User user, @PathVariable Integer dbId) {
        String password = databaseServiceFacade.getDatabasePassword(user, dbId);
        if (StringUtils.hasText(password)) {
            return ResponseEntity.ok(new DatabasePasswordForm(password));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{dbId}/reset")
    public ResponseEntity resetDatabase(@AuthenticationPrincipal User user, @PathVariable Integer dbId) {
        DatabaseInfoDTO dbInfo = databaseServiceFacade.resetDatabase(user, dbId);
        if (dbInfo != null) {
            return ResponseEntity.ok(dbInfo);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{dbId}/tariff")
    @JsonView(CommonView.CoreData.class)
    public ResponseEntity getDatabaseTariff(@AuthenticationPrincipal User user, @PathVariable Integer dbId) {
        DatabaseTariff tariff = databaseServiceFacade.getDatabaseTariff(user, dbId);
        return ResponseEntity.ok(tariff);
    }

    @PatchMapping("/{dbId}/tariff")
    public ResponseEntity changeDatabaseTariff(@AuthenticationPrincipal User user, @PathVariable Integer dbId,
                                               @Valid @RequestBody ChangeDatabaseTariffForm changeDatabaseTariffForm) {
        boolean changed = databaseServiceFacade.changeDatabaseTariff(user, dbId, changeDatabaseTariffForm.getTariffId());
        if (changed) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
