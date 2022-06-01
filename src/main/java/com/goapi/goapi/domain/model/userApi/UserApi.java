package com.goapi.goapi.domain.model.userApi;

import com.goapi.goapi.domain.model.Payment;
import com.goapi.goapi.domain.model.database.Database;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.userApi.request.UserApiRequest;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
public class UserApi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToMany(mappedBy = "userApi", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<UserApiRequest> userApiRequests = new LinkedHashSet<>();

    @NotBlank
    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "is_protected")
    private boolean isProtected;
    @Column
    private BigDecimal moneyAmount = BigDecimal.valueOf(0);

    @ManyToOne
    @JoinColumn(name = "database_id")
    private Database database;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @ManyToMany
    @JoinTable(name = "user_api_payments",
        joinColumns = @JoinColumn(name = "user_api_id"),
        inverseJoinColumns = @JoinColumn(name = "payments_id"))
    private Set<Payment> payments = new LinkedHashSet<>();

    public UserApi(String apiKey, boolean isProtected, Database database, String name, User owner) {
        this.apiKey = apiKey;
        this.isProtected = isProtected;
        this.database = database;
        this.name = name;
        this.owner = owner;
    }

    public UserApi() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserApi userApi = (UserApi) o;
        return id != null && Objects.equals(id, userApi.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}