package com.goapi.goapi.domain.dto.appServiceobject.database;

import com.example.DatabaseType;
import com.goapi.goapi.domain.dto.appServiceobject.AppServiceObjectDto;
import com.goapi.goapi.domain.dto.appServiceobject.AppServiceObjectStatusDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Daniil Dmitrochenkov
 **/
public class SummaryDatabaseDto extends AppServiceObjectDto implements Serializable {
    private final DatabaseType databaseType;

    public SummaryDatabaseDto(Integer id, String name, Date createdAt, AppServiceObjectStatusDto appServiceObjectStatusDto, DatabaseType databaseType) {
        super(id, name, createdAt, appServiceObjectStatusDto);
        this.databaseType = databaseType;
    }
}
