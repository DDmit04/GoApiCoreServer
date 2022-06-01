package com.goapi.goapi.domain.dto.api;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Daniil Dmitrochenkov
 **/
@Data
public class UserApiDto implements Serializable {

    private final Integer id;
    private final boolean isProtected;
    private final String name;
    private final List<UserApiRequestDto> requestDtoList;

}
