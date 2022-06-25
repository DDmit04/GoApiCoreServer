package com.goapi.goapi.domain.dto.finances;

import com.goapi.goapi.domain.model.finances.payment.paymentStatus.PaymentStatusType;
import lombok.Data;

import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Daniil Dmitrochenkov
 **/
@Data
public class BasePaymentDto implements Serializable {
    private final Integer id;
    private final Date date;
    private final PaymentStatusType paymentStatusType;
    @Positive(message = "pay sum must be positive!")
    private final BigDecimal sum;
    private final String description;
}
