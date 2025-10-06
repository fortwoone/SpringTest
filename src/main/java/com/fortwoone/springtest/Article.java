package com.fortwoone.springtest;

import java.util.Collection;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Getter
    @Setter
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="user_id")
    private User author;

    @Getter
    @Setter
    private Date publishDate;

    @Getter
    @Setter
    private String content;

    @Getter
    @OneToMany
    private Collection<UserOpinion> opinions;
}
