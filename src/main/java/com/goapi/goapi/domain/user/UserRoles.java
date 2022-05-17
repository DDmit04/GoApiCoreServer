package com.goapi.goapi.domain.user;

import org.springframework.security.core.GrantedAuthority;

public enum UserRoles implements GrantedAuthority {
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
