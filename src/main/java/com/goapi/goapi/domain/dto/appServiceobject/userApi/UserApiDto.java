package com.goapi.goapi.domain.dto.appServiceobject.userApi;

import com.goapi.goapi.domain.dto.appServiceobject.AppServiceObjectStatusDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Daniil Dmitrochenkov
 **/
public class UserApiDto extends SummaryUserApiDto implements Serializable {

    private final Integer databaseId;
    private final List<UserApiRequestDto> requestDtoList;

    public UserApiDto(Integer id, String name, Date createdAt, AppServiceObjectStatusDto appServiceObjectStatusDto, boolean isProtected, int requestsCount, Integer databaseId, List<UserApiRequestDto> requestDtoList) {
        super(id, name, createdAt, appServiceObjectStatusDto, isProtected, requestsCount);
        this.databaseId = databaseId;
        this.requestDtoList = requestDtoList;
    }
}
