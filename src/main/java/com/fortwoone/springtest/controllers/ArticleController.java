package com.fortwoone.springtest.controllers;

import com.fortwoone.springtest.model.Article;
import com.fortwoone.springtest.repositories.ArticleRepository;
import com.fortwoone.springtest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAnyRole('PUBLISHER', 'MODERATOR')")
    @PostMapping(path="/add")
    public @ResponseBody String createArticle(
            @RequestParam Integer authorID,
            @RequestParam String content
    ){
        Article a = new Article();
        a.setAuthor(userRepository.findById(authorID).orElseThrow());
        a.setContent(content);
        // Yes, this is mandatory to get the current date into a java.sql.Date object.
        // Yes, it is also stupid.
        a.setPublishDate(
                new Date(new java.util.Date().getTime())
        );
        articleRepository.save(a);
        return "Article saved";
    }

    @PreAuthorize("hasRole('MODERATOR') || hasRole('PUBLISHER') && articleRepository.findById(articleID).get().author.name == authentication.name")
    @DeleteMapping(path="/remove")
    public @ResponseBody String deleteArticle(@RequestParam Integer articleID){
        articleRepository.deleteById(articleID);
        return "Article deleted";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Article> getAllArticles(){
        return articleRepository.findAll();
    }
}
