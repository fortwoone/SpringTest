package com.fortwoone.springtest.json_mappings;

import java.sql.Date;

import com.fortwoone.springtest.model.Article;

public record ArticleLight(int userId, String author, Date publishDate, String content) {
    public ArticleLight(Article a){
        this(a.getAuthor().getId(), a.getAuthor().getUsername(), a.getPublishDate(), a.getContent());
    }
}
