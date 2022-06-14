package com.goapi.goapi.domain.model.finances.bill;

import com.goapi.goapi.domain.model.finances.payment.AppServiceBillPayment;
import com.goapi.goapi.domain.model.finances.payment.UserBillPayment;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "user_bill")
@Getter
@Setter
public class UserBill extends Bill {

    @OneToMany(orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Set<UserBillPayment> userBillPayments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "fromUserBill", fetch = FetchType.LAZY)
    private Set<AppServiceBillPayment> appServiceBillPayments = new LinkedHashSet<>();



}