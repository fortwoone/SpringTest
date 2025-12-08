package com.fortwoone.springtest.json_mappings;

import com.fortwoone.springtest.model.Article;

import java.sql.Date;

public record ArticleLight(int userId, String author, Date publishDate, String content) {
    public ArticleLight(Article a){
        this(a.getAuthor().getId(), a.getAuthor().getUsername(), a.getPublishDate(), a.getContent());
    }
}
