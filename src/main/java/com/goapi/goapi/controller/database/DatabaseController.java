package com.goapi.goapi.controller.database;

import com.goapi.goapi.controller.form.RenameForm;
import com.goapi.goapi.controller.form.database.CreateDatabaseForm;
import com.goapi.goapi.controller.form.database.GetDatabasePasswordForm;
import com.goapi.goapi.domain.dto.database.DatabaseDto;
import com.goapi.goapi.domain.dto.database.SummaryDatabaseDto;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.service.interfaces.database.DatabaseService;
import com.goapi.goapi.service.interfaces.facase.DatabaseServiceFacade;
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
    private final DatabaseService databaseService;


    @PostMapping
    public ResponseEntity<SummaryDatabaseDto> createNewDatabase(@AuthenticationPrincipal User user, @Valid @RequestBody CreateDatabaseForm createDbForm) {
        DatabaseDto databaseDto = databaseServiceFacade.createNewDatabase(user, createDbForm);
        return ResponseEntity.ok(databaseDto);
    }

    @GetMapping
    public ResponseEntity<List<SummaryDatabaseDto>> listDatabases(@AuthenticationPrincipal User user) {
        List<SummaryDatabaseDto> summaryDatabaseDtoList = databaseService.listUserDatabases(user);
        return ResponseEntity.ok(summaryDatabaseDtoList);
    }

    @PatchMapping("/{dbId}")
    public ResponseEntity<DatabaseDto> renameDatabase(@AuthenticationPrincipal User user, @Valid @RequestBody RenameForm renameForm, @PathVariable Integer dbId) {
        String newDatabaseName = renameForm.getName();
        boolean renamed = databaseServiceFacade.renameDatabase(user, dbId, newDatabaseName);
        if(renamed) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{dbId}")
    public ResponseEntity<DatabaseDto> getDatabaseInfo(@AuthenticationPrincipal User user, @PathVariable Integer dbId) {
        DatabaseDto databaseInfo = databaseServiceFacade.getDatabaseInfo(user, dbId);
        return ResponseEntity.ok(databaseInfo);
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

    @GetMapping("/{dbId}/password/reset")
    public ResponseEntity generateNewDbPassword(@AuthenticationPrincipal User user, @PathVariable Integer dbId) {
        boolean updated = databaseServiceFacade.generateNewDbPassword(user, dbId);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/{dbId}/password")
    public ResponseEntity<GetDatabasePasswordForm> getDbPassword(@AuthenticationPrincipal User user, @PathVariable Integer dbId) {
        String password = databaseServiceFacade.getDatabasePassword(user, dbId);
        if (StringUtils.hasText(password)) {
            GetDatabasePasswordForm passwordForm = new GetDatabasePasswordForm(password);
            return ResponseEntity.ok(passwordForm);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{dbId}/reset")
    public ResponseEntity<DatabaseDto> resetDatabase(@AuthenticationPrincipal User user, @PathVariable Integer dbId) {
        DatabaseDto dbInfo = databaseServiceFacade.resetDatabase(user, dbId);
        return ResponseEntity.ok(dbInfo);
    }

}
