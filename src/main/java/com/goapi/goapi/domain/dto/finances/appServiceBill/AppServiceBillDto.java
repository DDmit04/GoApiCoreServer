package com.goapi.goapi.domain.dto.finances.appServiceBill;

import com.goapi.goapi.domain.dto.finances.BaseBillDto;
import com.goapi.goapi.domain.dto.finances.BasePaymentDto;
import com.goapi.goapi.domain.model.finances.bill.BillType;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class AppServiceBillDto extends BaseBillDto implements Serializable {
    private final BillType billType;
    private final List<BasePaymentDto> toAppServiceObjectBillPayments;

    private final List<BasePaymentDto> appServicePayouts;

    public AppServiceBillDto(Integer id, BigDecimal moneyLeft, BillType billType, List<BasePaymentDto> toAppServiceObjectBillPayments, List<BasePaymentDto> appServicePayouts) {
        super(id, moneyLeft);
        appServicePayouts.sort(Comparator.comparing(BasePaymentDto::getDate));
        toAppServiceObjectBillPayments.sort(Comparator.comparing(BasePaymentDto::getDate));
        this.billType = billType;
        this.toAppServiceObjectBillPayments = toAppServiceObjectBillPayments;
        this.appServicePayouts = appServicePayouts;
    }
}
