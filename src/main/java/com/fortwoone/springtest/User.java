package com.fortwoone.springtest;

import java.util.Collection;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity // This tells Hibernate to make a table out of this class
public class User {
    @Setter
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
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
}