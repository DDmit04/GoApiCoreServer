package com.goapi.goapi.domain.api;

import com.goapi.goapi.domain.api.request.UserApiRequest;
import com.goapi.goapi.domain.database.Database;
import com.goapi.goapi.domain.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
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

    @OneToMany(mappedBy = "userApi", orphanRemoval = true)
    private Set<UserApiRequest> userApiRequests = new LinkedHashSet<>();

    @NotBlank
    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "is_protected")
    private boolean isProtected;

    @ManyToOne
    @JoinColumn(name = "api_tariff_id")
    private UserApiTariff userApiTariff;

    @ManyToOne
    @JoinColumn(name = "database_id")
    private Database database;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    public UserApi(String apiKey, boolean isProtected, UserApiTariff userApiTariff, Database database, String name, User owner) {
        this.apiKey = apiKey;
        this.isProtected = isProtected;
        this.userApiTariff = userApiTariff;
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