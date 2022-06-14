package com.goapi.goapi.domain.model.finances.bill;

import com.goapi.goapi.domain.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;

@MappedSuperclass
@Getter
@Setter
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Access(AccessType.PROPERTY)
    private Integer id;

    @Column(name = "money_left", precision = 19, scale = 2)
    private BigDecimal moneyLeft;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Bill(User user) {
        this.user = user;
        this.moneyLeft = BigDecimal.ZERO;
    }

    public Bill() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bill)) return false;

        Bill bill = (Bill) o;

        return getId() != null ? getId().equals(bill.getId()) : bill.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}