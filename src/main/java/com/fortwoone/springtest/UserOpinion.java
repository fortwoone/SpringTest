package com.fortwoone.springtest;

import jakarta.persistence.*;
import java.util.Collection;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;


@Entity
public class UserOpinion {
    @Id
    @Getter
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name="article_id", nullable = false)
    private Article article;

    @Getter
    @NonNull
    @Setter
    private Boolean liked;
}
