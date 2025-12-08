package com.fortwoone.springtest.model;

import java.util.Collection;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @ManyToOne(cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
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
