package com.fortwoone.springtest.model;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Entity // This tells Hibernate to make a table out of this class
public class User implements UserDetails {
    @Setter
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Setter
    private String name;

    @Setter
    private String password;

    @Setter
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany
    private Collection<UserOpinion> opinions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getUsername() {
        return name;
    }
}