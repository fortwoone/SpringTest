package com.fortwoone.springtest;

import java.util.Collection;

import jakarta.persistence.*;
import lombok.Getter;

import java.sql.Date;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="user_id")
    private User author;

    @Getter
    private Date publishDate;

    @Getter
    private String content;

    @Getter
    @OneToMany
    private Collection<UserOpinion> opinions;
}
