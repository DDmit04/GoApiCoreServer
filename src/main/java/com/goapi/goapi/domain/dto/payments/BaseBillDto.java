package com.goapi.goapi.domain.dto.payments;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Daniil Dmitrochenkov
 **/
@Data
public class BaseBillDto {
    private final Integer id;
    private final BigDecimal moneyLeft;
}
