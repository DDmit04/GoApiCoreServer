package com.goapi.goapi.domain.model.bill;

import com.goapi.goapi.domain.model.payment.UserBillPayment;
import com.goapi.goapi.domain.model.payment.UserBillToAppServiceBillPayment;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
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

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private Set<UserBillPayment> userBillPayments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "fromUserBill", orphanRemoval = false)
    private Set<UserBillToAppServiceBillPayment> userBillToAppServiceBillPayments = new LinkedHashSet<>();



}