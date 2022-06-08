package com.goapi.goapi.domain.model.bill;

import com.goapi.goapi.domain.model.payment.ExternalPayment;
import com.goapi.goapi.domain.model.payment.InternalPayment;
import com.goapi.goapi.domain.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "bill_type")
    private BillType billType;

    @Column(name = "money_left", precision = 19, scale = 2)
    private BigDecimal moneyLeft;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private Set<ExternalPayment> externalPayments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "fromBill", orphanRemoval = true)
    private Set<InternalPayment> internalPaymentsFromBill = new LinkedHashSet<>();

    @OneToMany(mappedBy = "toBill", orphanRemoval = true)
    private Set<InternalPayment> internalPaymentsToBill = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Bill(BillType billType) {
        this.billType = billType;
    }

    public Bill(User user, BillType billType) {
        this.billType = billType;
        this.user = user;
    }

    public Bill() {
    }
}