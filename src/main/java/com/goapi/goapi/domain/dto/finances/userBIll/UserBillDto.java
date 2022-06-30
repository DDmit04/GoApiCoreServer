package com.goapi.goapi.domain.dto.finances.userBIll;

import com.goapi.goapi.domain.dto.finances.BaseBillDto;
import com.goapi.goapi.domain.dto.finances.BasePaymentDto;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
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
        userBillPayments.sort(Comparator.comparing(BasePaymentDto::getDate));
        appServiceBillPayments.sort(Comparator.comparing(BasePaymentDto::getDate));
        this.userBillPayments = userBillPayments;
        this.appServiceBillPayments = appServiceBillPayments;
    }
}
