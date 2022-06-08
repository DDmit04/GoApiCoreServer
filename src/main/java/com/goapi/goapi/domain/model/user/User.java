package com.goapi.goapi.domain.model.user;

import com.goapi.goapi.domain.model.database.Database;
import com.goapi.goapi.domain.model.payment.ExternalPayment;
import com.goapi.goapi.domain.model.payment.InternalPayment;
import com.goapi.goapi.domain.model.token.SecurityToken;
import com.goapi.goapi.domain.model.userApi.UserApi;
import com.goapi.goapi.domain.model.userApi.UserApiTariff;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Daniil Dmitrochenkov
 **/
@Entity
@Getter
@Setter
@Table(name = "usr")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    @NotBlank(message = "username can't be blank!")
    private String username;
    @NotBlank(message = "password can't be blank!")
    private String userPassword;
    @NotBlank(message = "email can't be blank!")
    @Email
    @Column(unique = true)
    private String email;
    @Column
    private String jwtRefreshToken;
    @Column
    private BigDecimal moneyAmount = BigDecimal.valueOf(0);

    @OneToMany(mappedBy = "owner", orphanRemoval = true)
    private Set<Database> databases = new LinkedHashSet<>();

    @ElementCollection(targetClass = UserRoles.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRoles> roles;

    @OneToMany(mappedBy = "owner", orphanRemoval = true)
    private Set<UserApi> userApis = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_api_tariff_id")
    private UserApiTariff userApiTariff;

    @Column(name = "is_email_confirmed")
    private boolean isEmailConfirmed;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<SecurityToken> securityTokens = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<InternalPayment> internalPayments = new LinkedHashSet<>();

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private Set<ExternalPayment> externalPayments = new LinkedHashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!getId().equals(user.getId())) return false;
        if (!getUsername().equals(user.getUsername())) return false;
        return getEmail().equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getUsername().hashCode();
        result = 31 * result + getEmail().hashCode();
        return result;
    }
}
