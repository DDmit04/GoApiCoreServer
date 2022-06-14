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
public class EmailSecurityToken extends SecurityToken {

    @Column(name = "confirming_email")
    private String confirmingEmail;

    public EmailSecurityToken(String token, Date expire, User user, String confirmingEmail) {
        super(token, expire, user);
        this.confirmingEmail = confirmingEmail;
    }

    public EmailSecurityToken() {
    }
}