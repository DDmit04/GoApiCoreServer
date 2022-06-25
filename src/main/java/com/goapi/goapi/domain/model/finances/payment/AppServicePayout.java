package com.goapi.goapi.domain.model.finances.payment;

import com.goapi.goapi.domain.entityListeners.AppServicePayoutEntityListener;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@EntityListeners(AppServicePayoutEntityListener.class)
public class AppServicePayout extends Payment {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "app_service_bill_id")
    private AppServiceBill appServiceBill;

    public AppServicePayout(BigDecimal sum, String description, AppServiceBill appServiceBill) {
        super(sum, description);
        this.appServiceBill = appServiceBill;
    }

    public AppServicePayout() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AppServicePayout that = (AppServicePayout) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
