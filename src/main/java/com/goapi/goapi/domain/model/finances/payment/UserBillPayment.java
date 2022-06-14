package com.goapi.goapi.domain.model.finances.payment;

import com.goapi.goapi.domain.model.finances.bill.UserBill;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
public class UserBillPayment extends Payment {

    @OneToOne(orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_bill_id")
    private UserBill userBill;

    public UserBillPayment(BigDecimal sum, String description, UserBill userBill) {
        super(sum, description);
        this.userBill = userBill;
    }

    public UserBillPayment() {
    }

    @PrePersist
    public void afterInsert() {
        BigDecimal userBillMoney = userBill.getMoneyLeft();
        userBill.setMoneyLeft(userBillMoney.add(getSum()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserBillPayment that = (UserBillPayment) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
