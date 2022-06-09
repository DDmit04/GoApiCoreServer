package com.goapi.goapi.domain.model.payment;

import com.goapi.goapi.domain.model.bill.AppServiceBill;
import com.goapi.goapi.domain.model.bill.UserBill;
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
public class UserBillToAppServiceBillPayment extends Payment {

    @ManyToOne
    @JoinColumn(name = "from_user_bill_id")
    private UserBill fromUserBill;

    @ManyToOne
    @JoinColumn(name = "to_service_bill_id")
    private AppServiceBill toAppServiceBill;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserBillToAppServiceBillPayment that = (UserBillToAppServiceBillPayment) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
