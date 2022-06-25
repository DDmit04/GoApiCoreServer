package com.goapi.goapi.domain.model.user.token;

import com.goapi.goapi.domain.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
public abstract class SecurityToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "security token string can't be blank!")
    @Column(nullable = false, name = "token")
    private String token;

    @NotNull(message = "security token expire date can't be null!")
    @Column(nullable = false, name = "expire")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expire;

    @Column(nullable = false, name = "valid")
    private boolean isValid;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public SecurityToken(String token, Date expire, User user) {
        this.token = token;
        this.expire = expire;
        this.isValid = true;
        this.user = user;
    }

    public SecurityToken() {
    }

    public boolean isExpired() {
        return new Date().after(this.expire);
    }

    public boolean isIsValid() {
        return !isExpired() && isValid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SecurityToken that = (SecurityToken) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}