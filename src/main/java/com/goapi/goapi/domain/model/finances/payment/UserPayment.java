package com.goapi.goapi.domain.model.finances.payment;

import com.goapi.goapi.domain.entityListeners.UserPaymentEntityListener;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@EntityListeners(UserPaymentEntityListener.class)
public class UserPayment extends Payment {

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_bill_id")
    private UserBill userBill;

    public UserPayment(BigDecimal sum, String description, UserBill userBill) {
        super(sum, description);
        this.userBill = userBill;
    }

    public UserPayment() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserPayment that = (UserPayment) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
