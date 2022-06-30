package com.goapi.goapi.domain.model.finances.bill;

import com.goapi.goapi.domain.model.appService.AppServiceObject;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayment;
import com.goapi.goapi.domain.model.finances.payment.AppServicePayout;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "app_service_bill")
@Getter
@Setter
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "AppServiceBill.payments",
        attributeNodes = {
            @NamedAttributeNode("toAppServicePayments"),
            @NamedAttributeNode("appServicePayouts")
        }
    )
})
public class AppServiceBill extends Bill {
    @NotNull(message = "bill type can't be null!")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "bill_type")
    @Access(AccessType.PROPERTY)
    private BillType billType;

    @Column(nullable = false, name = "last_payout_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPayoutDate;

    @OneToOne(optional = false, fetch = FetchType.LAZY, mappedBy = "appServiceBill")
    private AppServiceObject appServiceObject;

    @OneToMany(mappedBy = "toAppServiceBill", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<AppServicePayment> toAppServicePayments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "appServiceBill", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<AppServicePayout> appServicePayouts = new LinkedHashSet<>();

    public AppServiceBill(BillType billType) {
        this.lastPayoutDate = new Date();
        this.billType = billType;
    }

    public AppServiceBill() {
        this.lastPayoutDate = new Date();
    }

    @Override
    public String toString() {
        return "(AppServiceBill{" +
            "billType=" + billType +
            ", lastPayoutDate=" + lastPayoutDate +
            '}' + super.toString() + ')';
    }
}