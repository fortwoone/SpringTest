package com.fortwoone.springtest.json_mappings;

import java.sql.Date;
import java.util.Collection;

import com.fortwoone.springtest.model.Article;
import com.fortwoone.springtest.model.UserOpinion;


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
