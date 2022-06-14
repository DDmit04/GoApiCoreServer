package com.goapi.goapi.domain.model.finances.payment;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Daniil Dmitrochenkov
 **/
@MappedSuperclass
@Getter
@Setter
public abstract class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private LocalDateTime date;
    @Positive(message = "pay sum must be positive!")
    private BigDecimal sum;
    @Column(name = "description")
    private String description;

    public Payment(BigDecimal sum, String description) {
        this.sum = sum;
        this.description = description;
        this.date = LocalDateTime.now();
    }

    public Payment() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Payment payment = (Payment) o;
        return id != null && Objects.equals(id, payment.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
