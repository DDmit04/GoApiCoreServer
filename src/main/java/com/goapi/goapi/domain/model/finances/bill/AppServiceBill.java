package com.goapi.goapi.domain.model.finances.bill;

import com.goapi.goapi.domain.model.finances.payment.AppServiceBillPayment;
import com.goapi.goapi.domain.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "app_service_bill")
@Getter
@Setter
public class AppServiceBill extends Bill {
    @Enumerated(EnumType.STRING)
    @Column(name = "bill_type")
    private BillType billType;

    @OneToMany(mappedBy = "toAppServiceBill", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<AppServiceBillPayment> toAppServicePayments = new LinkedHashSet<>();

    public AppServiceBill(User user, BillType billType) {
        super(user);
        this.billType = billType;
    }

    public AppServiceBill() {
    }

}