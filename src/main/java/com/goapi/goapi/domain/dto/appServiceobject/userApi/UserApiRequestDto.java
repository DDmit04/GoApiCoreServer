package com.goapi.goapi.domain.dto.appServiceobject.userApi;

import com.goapi.goapi.domain.dto.appServiceobject.userApi.summary.SummaryUserApiRequestDto;
import lombok.Getter;

import java.util.List;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class UserApiRequestDto extends SummaryUserApiRequestDto {

    private final List<UserApiRequestArgumentDto> arguments;

    public UserApiRequestDto(SummaryUserApiRequestDto summaryDto, List<UserApiRequestArgumentDto> arguments) {
        super(summaryDto.getId(), summaryDto.getRequestName(), summaryDto.getRequestTemplate(), summaryDto.getHttpMethod(), summaryDto.getRequestUrl());
        this.arguments = arguments;
    }
}
