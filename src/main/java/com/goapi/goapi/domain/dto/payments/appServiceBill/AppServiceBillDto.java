package com.goapi.goapi.domain.dto.payments.appServiceBill;

import com.goapi.goapi.domain.dto.payments.BaseBillDto;
import com.goapi.goapi.domain.dto.payments.BasePaymentDto;
import com.goapi.goapi.domain.model.finances.bill.BillType;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class AppServiceBillDto extends BaseBillDto implements Serializable {
    private final BillType billType;
    private final List<BasePaymentDto> toAppServiceObjectBill;

    public AppServiceBillDto(Integer id, BigDecimal moneyLeft, BillType billType, List<BasePaymentDto> toAppServiceObjectBill) {
        super(id, moneyLeft);
        this.billType = billType;
        this.toAppServiceObjectBill = toAppServiceObjectBill;
    }
}
