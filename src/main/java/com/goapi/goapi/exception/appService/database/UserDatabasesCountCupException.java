package com.goapi.goapi.exception.appService.database;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniil Dmitrochenkov
 **/
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "User databases count is max for any user")
public class UserDatabasesCountCupException extends RuntimeException {

    public UserDatabasesCountCupException(Integer userId) {
        super(String.format("User with id = '%s' can't add new database, because reach max databases count for any user!", userId));
    }
}
