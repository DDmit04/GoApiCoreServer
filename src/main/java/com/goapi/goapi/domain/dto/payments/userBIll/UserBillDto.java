package com.goapi.goapi.domain.dto.payments.userBIll;

import com.goapi.goapi.domain.dto.payments.BaseBillDto;
import com.goapi.goapi.domain.dto.payments.BasePaymentDto;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class UserBillDto extends BaseBillDto implements Serializable {
    private final List<BasePaymentDto> userBillPayments;
    private final List<BasePaymentDto> appServiceBillPayments;

    public UserBillDto(Integer id, BigDecimal moneyLeft, List<BasePaymentDto> userBillPayments, List<BasePaymentDto> appServiceBillPayments) {
        super(id, moneyLeft);
        this.userBillPayments = userBillPayments;
        this.appServiceBillPayments = appServiceBillPayments;
    }
}
