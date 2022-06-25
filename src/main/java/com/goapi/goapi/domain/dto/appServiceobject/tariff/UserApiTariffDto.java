package com.goapi.goapi.domain.dto.appServiceobject.tariff;

import lombok.Getter;

import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class UserApiTariffDto extends TariffDto implements Serializable {
    @PositiveOrZero
    private final Integer maxRequestsCount;

    public UserApiTariffDto(Integer id, String name, BigDecimal costPerMonth, Integer maxRequestsCount) {
        super(id, name, costPerMonth);
        this.maxRequestsCount = maxRequestsCount;
    }
}
