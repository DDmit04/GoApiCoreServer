package com.goapi.goapi.domain.model.finances.payment;

import com.goapi.goapi.domain.model.finances.payment.paymentStatus.PaymentStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Date;
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
    @NotNull(message = "payment date can't be null!")
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Positive(message = "pay sum must be positive!")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal sum;
    @NotBlank(message = "payment description can't be blank!")
    @Column(nullable = false, name = "description")
    private String description;

    @Embedded
    private PaymentStatus status;

    public Payment(BigDecimal sum, String description) {
        this.sum = sum;
        this.description = description;
        this.date = new Date();
        this.status = new PaymentStatus();
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

    @Override
    public String toString() {
        return "Payment{" +
            "id=" + id +
            ", date=" + date +
            ", sum=" + sum +
            ", description='" + description + '\'' +
            ", status=" + status +
            '}';
    }
}
