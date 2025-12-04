package com.fortwoone.springtest.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    MODERATOR,
    PUBLISHER;


    @Override
    public String getAuthority() {
        return "ROLE_" + this.toString();
    }
}
