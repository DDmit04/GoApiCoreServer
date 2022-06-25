package com.goapi.goapi.domain.dto.appServiceobject.tariff;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Daniil Dmitrochenkov
 **/
@Data
public class TariffDto {
    private final Integer id;
    private final String name;
    private final BigDecimal costPerMonth;
}
