package com.goapi.goapi.controller.controllers.database;

import com.goapi.goapi.controller.forms.RenameForm;
import com.goapi.goapi.controller.forms.database.CreateDatabaseRequest;
import com.goapi.goapi.controller.forms.user.PasswordForm;
import com.goapi.goapi.domain.dto.appServiceobject.database.DatabaseDto;
import com.goapi.goapi.domain.dto.appServiceobject.database.SummaryDatabaseDto;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.service.interfaces.facade.database.DatabaseServiceFacade;
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

/**
 * @author Daniil Dmitrochenkov
 **/
@RestController
@RequestMapping("/user/db")
@RequiredArgsConstructor
public class DatabaseController {

    private final DatabaseServiceFacade databaseServiceFacade;

    @PostMapping
    public ResponseEntity<SummaryDatabaseDto> createDatabase(@AuthenticationPrincipal User user, @Valid @RequestBody CreateDatabaseRequest createDbForm) {
        DatabaseDto databaseDto = databaseServiceFacade.createNewDatabase(user, createDbForm);
        return ResponseEntity.ok(databaseDto);
    }

    @DeleteMapping("/{dbId}")
    public ResponseEntity deleteDatabase(@AuthenticationPrincipal User user, @PathVariable Integer dbId) {
        boolean deleted = databaseServiceFacade.deleteDatabase(user, dbId);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{dbId}/reset")
    public ResponseEntity<DatabaseDto> resetDatabase(@AuthenticationPrincipal User user, @PathVariable Integer dbId) {
        DatabaseDto dbInfo = databaseServiceFacade.resetDatabase(user, dbId);
        return ResponseEntity.ok(dbInfo);
    }

    @PatchMapping("/{dbId}")
    public ResponseEntity<DatabaseDto> renameDatabase(@AuthenticationPrincipal User user, @Valid @RequestBody RenameForm renameForm, @PathVariable Integer dbId) {
        String newDatabaseName = renameForm.getName();
        databaseServiceFacade.renameDatabase(user, dbId, newDatabaseName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{dbId}")
    public ResponseEntity<DatabaseDto> getDatabaseInfo(@AuthenticationPrincipal User user, @PathVariable Integer dbId) {
        DatabaseDto databaseInfo = databaseServiceFacade.getDatabaseInfo(user, dbId);
        return ResponseEntity.ok(databaseInfo);
    }

    @GetMapping
    public ResponseEntity<List<SummaryDatabaseDto>> listDatabases(@AuthenticationPrincipal User user) {
        List<SummaryDatabaseDto> summaryDatabaseDtoList = databaseServiceFacade.listUserDatabasesDtos(user);
        return ResponseEntity.ok(summaryDatabaseDtoList);
    }

    @GetMapping("/{dbId}/password/reset")
    public ResponseEntity generateNewPasswordForDatabase(@AuthenticationPrincipal User user, @PathVariable Integer dbId) {
        databaseServiceFacade.generateNewDatabasePassword(user, dbId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{dbId}/password")
    public ResponseEntity<PasswordForm> getDatabasePassword(@AuthenticationPrincipal User user, @PathVariable Integer dbId) {
        String password = databaseServiceFacade.getDatabasePassword(user, dbId);
        if (StringUtils.hasText(password)) {
            PasswordForm passwordForm = new PasswordForm(password);
            return ResponseEntity.ok(passwordForm);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{dbId}/allow")
    public ResponseEntity allowExternalConnections(@AuthenticationPrincipal User user, @PathVariable Integer dbId) {
        databaseServiceFacade.allowDatabaseExternalConnections(user, dbId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{dbId}/deny")
    public ResponseEntity denyExternalConnections(@AuthenticationPrincipal User user, @PathVariable Integer dbId) {
        databaseServiceFacade.denyDatabaseExternalConnections(user, dbId);
        return ResponseEntity.ok().build();
    }

}
