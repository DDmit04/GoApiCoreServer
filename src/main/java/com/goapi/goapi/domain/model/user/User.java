package com.goapi.goapi.domain.model.user;

import com.goapi.goapi.domain.model.appService.database.Database;
import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.user.token.EmailSecurityToken;
import com.goapi.goapi.domain.model.user.token.PasswordSecurityToken;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
    @Access(AccessType.PROPERTY)
    private Integer id;

    @NotBlank(message = "username can't be blank!")
    @Column(nullable = false, unique = true)
    private String username;
    @NotBlank(message = "password can't be blank!")
    @Column(nullable = false)
    private String userPassword;
    @NotBlank(message = "email can't be blank!")
    @Email(message = "user email must be email!")
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, name = "is_email_confirmed")
    private boolean isEmailConfirmed;
    @ElementCollection(targetClass = UserRoles.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRoles> roles;
    @NotBlank(message = "usr JWT refresh token cant be blank!")
    @Column(nullable = false)
    private String jwtRefreshToken;

    @OneToMany(mappedBy = "owner", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Database> databases = new LinkedHashSet<>();
    @OneToMany(mappedBy = "owner", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<UserApi> userApis = new LinkedHashSet<>();

    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false, name = "bill_id")
    private UserBill userBill;

    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<EmailSecurityToken> emailSecurityTokens = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<PasswordSecurityToken> passwordSecurityTokens = new LinkedHashSet<>();

    public User(String username, String userPassword, String email, Set<UserRoles> roles, UserBill userBill, String jwtRefreshToken) {
        this.username = username;
        this.userPassword = userPassword;
        this.email = email;
        this.roles = roles;
        this.userBill = userBill;
        this.jwtRefreshToken = jwtRefreshToken;
    }

    public User() {
    }

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

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", userPassword='" + userPassword + '\'' +
            ", email='" + email + '\'' +
            ", isEmailConfirmed=" + isEmailConfirmed +
            ", roles=" + roles +
            '}';
    }
}
