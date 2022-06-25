package com.goapi.goapi.domain.dto.appServiceobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Daniil Dmitrochenkov
 **/
@Data
public class AppServiceObjectDto implements Serializable {

    private final Integer id;
    private final String name;
    private final Date createdAt;
    private final AppServiceObjectStatusDto appServiceObjectStatusDto;

}
