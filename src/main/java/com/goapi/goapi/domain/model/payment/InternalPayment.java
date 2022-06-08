package com.goapi.goapi.domain.model.payment;

import com.goapi.goapi.domain.model.bill.Bill;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author Daniil Dmitrochenkov
 **/
@Table
@Getter
@Setter
@Entity
public class InternalPayment extends Payment {

    @ManyToOne
    @JoinColumn(name = "from_bill_id")
    private Bill fromBill;

    @ManyToOne
    @JoinColumn(name = "to_bill_id")
    private Bill toBill;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InternalPayment that = (InternalPayment) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
