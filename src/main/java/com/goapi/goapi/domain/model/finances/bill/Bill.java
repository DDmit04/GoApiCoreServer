package com.goapi.goapi.domain.model.finances.bill;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Access(AccessType.PROPERTY)
    private Integer id;

    @NotNull(message = "bill money left can't be null!")
    @Column(nullable = false, name = "money_left", precision = 19, scale = 2)
    @Access(AccessType.PROPERTY)
    private BigDecimal moneyLeft;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date created;

    public Bill() {
        this.created = new Date();
        this.moneyLeft = BigDecimal.ZERO;
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

    @Override
    public String toString() {
        return "Bill{" +
            "id=" + id +
            ", moneyLeft=" + moneyLeft +
            ", created=" + created +
            '}';
    }
}