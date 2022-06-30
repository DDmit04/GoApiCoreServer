package com.goapi.goapi.domain.dto.appServiceobject.userApi.summary;

import com.goapi.goapi.domain.dto.appServiceobject.AppServiceObjectDto;
import com.goapi.goapi.domain.dto.appServiceobject.AppServiceObjectStatusDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Daniil Dmitrochenkov
 **/
public class SummaryUserApiDto extends AppServiceObjectDto implements Serializable {
    private final boolean isProtected;
    private final int requestsCount;

    public SummaryUserApiDto(Integer id, String name, Date createdAt, AppServiceObjectStatusDto appServiceObjectStatusDto, boolean isProtected, int requestsCount) {
        super(id, name, createdAt, appServiceObjectStatusDto);
        this.isProtected = isProtected;
        this.requestsCount = requestsCount;
    }
}
