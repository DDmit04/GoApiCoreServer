package com.goapi.goapi.domain.dto.appServiceobject.userApi;

import com.goapi.goapi.domain.dto.appServiceobject.AppServiceObjectStatusDto;
import com.goapi.goapi.domain.dto.appServiceobject.userApi.summary.SummaryUserApiDto;
import com.goapi.goapi.domain.dto.appServiceobject.userApi.summary.SummaryUserApiRequestDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Daniil Dmitrochenkov
 **/
public class UserApiDto extends SummaryUserApiDto implements Serializable {

    private final Integer databaseId;
    private final List<SummaryUserApiRequestDto> requestDtoList;

    public UserApiDto(Integer id, String name, Date createdAt, AppServiceObjectStatusDto appServiceObjectStatusDto, boolean isProtected, int requestsCount, Integer databaseId, List<SummaryUserApiRequestDto> requestDtoList) {
        super(id, name, createdAt, appServiceObjectStatusDto, isProtected, requestsCount);
        this.databaseId = databaseId;
        this.requestDtoList = requestDtoList;
    }
}
