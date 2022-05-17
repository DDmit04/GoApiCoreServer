package com.goapi.goapi.domain.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.goapi.goapi.domain.Payment;
import com.goapi.goapi.domain.api.UserApi;
import com.goapi.goapi.domain.database.Database;
import com.goapi.goapi.views.CommonView;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

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
    @JsonView(CommonView.Id.class)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "dbUsername can't be blank!")
    @JsonView(CommonView.CoreData.class)
    private String username;

    @Column(unique = true)
    @NotBlank(message = "internal dbUsername can't be blank!")
    @JsonView(CommonView.FullData.class)
    private String internalUsername;

    @NotBlank(message = "password can't be blank!")
    @JsonView(CommonView.FullData.class)
    private String password;

    @NotBlank(message = "email can't be blank!")
    @Column(unique = true)
    @JsonView(CommonView.CoreData.class)
    private String email;

    @Column
    @JsonView(CommonView.FullData.class)
    private UUID refreshTokenUuid;

    @Column()
    @JsonView(CommonView.CoreData.class)
    private BigDecimal moneyAmount = BigDecimal.valueOf(0);

    @OneToMany(mappedBy = "owner", orphanRemoval = true)
    @JsonView(CommonView.FullData.class)
    private Set<Database> databases = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonView(CommonView.FullData.class)
    private Set<Payment> payments = new LinkedHashSet<>();

    @ElementCollection(targetClass = UserRoles.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @JsonView(CommonView.FullData.class)
    private Set<UserRoles> roles;

    @OneToMany(mappedBy = "owner", orphanRemoval = true)
    private Set<UserApi> userApis = new LinkedHashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
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
