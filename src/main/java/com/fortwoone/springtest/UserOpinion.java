package com.fortwoone.springtest;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;

@Entity
public class UserOpinion {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="article_id", nullable = false)
    private Article article;

    @NonNull
    private Boolean liked;
}
