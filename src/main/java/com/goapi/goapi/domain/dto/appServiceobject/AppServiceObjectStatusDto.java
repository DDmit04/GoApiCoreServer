package com.goapi.goapi.domain.dto.appServiceobject;

import com.goapi.goapi.domain.model.appService.AppServiceObjectStatus;
import com.goapi.goapi.domain.model.appService.AppServiceStatusType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Daniil Dmitrochenkov
 **/
@Data
public class AppServiceObjectStatusDto implements Serializable {
    private final AppServiceStatusType status;
    private final Date statusDate;

    public AppServiceObjectStatusDto(AppServiceObjectStatus appServiceObjectStatus) {
        this.status = appServiceObjectStatus.getStatus();
        this.statusDate = appServiceObjectStatus.getStatusDate();
    }

    public AppServiceObjectStatusDto(AppServiceStatusType status, Date statusDate) {
        this.status = status;
        this.statusDate = statusDate;
    }
}
