package com.goapi.goapi.domain.dto.appServiceobject.tariff;

import lombok.Getter;

import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class DatabaseTariffDto extends TariffDto implements Serializable {
    @PositiveOrZero(message = "db size must be more than 0!")
    private final long maxSizeBytes;

    public DatabaseTariffDto(Integer id, String name, BigDecimal costPerMonth, long maxSizeBytes) {
        super(id, name, costPerMonth);
        this.maxSizeBytes = maxSizeBytes;
    }
}
