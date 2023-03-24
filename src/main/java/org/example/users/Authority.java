package org.example.users;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {

    LOCKED, USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
