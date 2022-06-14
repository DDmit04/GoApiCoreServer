package com.goapi.goapi.domain.model.user.token;

import com.goapi.goapi.domain.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table
@Getter
@Setter
public class PasswordSecurityToken extends SecurityToken {
    @Column(name = "old_password")
    private String oldPassword;

    public PasswordSecurityToken(String token, Date expire, User user, String oldPassword) {
        super(token, expire, user);
        this.oldPassword = oldPassword;
    }

    public PasswordSecurityToken() {
    }
}
