package com.goapi.goapi.domain.model.finances.payment;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Daniil Dmitrochenkov
 **/
@Table
@Getter
@Setter
@Entity
public class AppServiceBillPayment extends Payment {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_bill_id")
    private UserBill fromUserBill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_service_bill_id")
    private AppServiceBill toAppServiceBill;

    public AppServiceBillPayment(BigDecimal sum, String description, UserBill fromUserBill, AppServiceBill toAppServiceBill) {
        super(sum, description);
        this.fromUserBill = fromUserBill;
        this.toAppServiceBill = toAppServiceBill;
    }

    public AppServiceBillPayment() {
    }

    @PrePersist
    public void afterInsert() {
        BigDecimal userBillMoney = fromUserBill.getMoneyLeft();
        BigDecimal appServiceMoney = toAppServiceBill.getMoneyLeft();
        BigDecimal paymentSum = getSum();
        fromUserBill.setMoneyLeft(userBillMoney.subtract(paymentSum));
        toAppServiceBill.setMoneyLeft(appServiceMoney.add(paymentSum));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AppServiceBillPayment that = (AppServiceBillPayment) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
