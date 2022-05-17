package com.goapi.goapi.security.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import javax.security.auth.Subject;
import java.util.ArrayList;

/**
 * @author Daniil Dmitrochenkov
 **/
public class JwtProvedAuthToken extends AbstractAuthenticationToken {

    private String username;

    public JwtProvedAuthToken(String username) {
        super(new ArrayList<>());
        this.username = username;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }
}
