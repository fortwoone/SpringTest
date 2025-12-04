package com.fortwoone.springtest.model;

import java.sql.Date;
import java.util.Collection;

public record ReturnedArticle(int authorId, String author, Date publishDate, String content, Collection<UserOpinion> opinions) {
    public ReturnedArticle(Article a){
        this(
            a.getAuthor().getId(),
            a.getAuthor().getUsername(),
            a.getPublishDate(),
            a.getContent(),
            a.getOpinions()
        );
    }
}
