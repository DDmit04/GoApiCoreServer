package com.goapi.goapi.domain.model.finances.bill;

import com.goapi.goapi.domain.model.finances.payment.AppServicePayment;
import com.goapi.goapi.domain.model.finances.payment.UserPayment;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "user_bill")
@Getter
@Setter
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "UserBill.payments",
        attributeNodes = {
            @NamedAttributeNode("userPayments"),
            @NamedAttributeNode("appServicePayments")
        }
    )
})
public class UserBill extends Bill {

    @OneToMany(mappedBy = "userBill", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<UserPayment> userPayments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "fromUserBill", fetch = FetchType.LAZY)
    private Set<AppServicePayment> appServicePayments = new LinkedHashSet<>();


    public UserBill() {

    }
}