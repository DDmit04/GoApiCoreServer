package com.goapi.goapi.domain.model.payment;

import com.goapi.goapi.domain.model.bill.UserBill;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author Daniil Dmitrochenkov
 **/
@Table
@Getter
@Setter
@Entity
public class UserBillPayment extends Payment {

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "user_bill_id")
    private UserBill userBill;

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
