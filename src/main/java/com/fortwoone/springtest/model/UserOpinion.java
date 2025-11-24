package com.fortwoone.springtest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;


@Getter
@Entity
public class UserOpinion {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Setter
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Setter
    @ManyToOne
    @JoinColumn(name="article_id", nullable = false)
    private Article article;

    @Setter
    private Boolean liked;
}
