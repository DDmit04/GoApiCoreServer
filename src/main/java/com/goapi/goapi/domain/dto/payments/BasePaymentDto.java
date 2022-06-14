package com.goapi.goapi.domain.dto.payments;

import lombok.Data;

import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Daniil Dmitrochenkov
 **/
@Data
public class BasePaymentDto implements Serializable {
    private final Integer id;
    private final LocalDateTime date;
    @Positive(message = "pay sum must be positive!")
    private final BigDecimal sum;
    private final String description;
}
