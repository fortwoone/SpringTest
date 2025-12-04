package com.fortwoone.springtest.controllers;

import com.fortwoone.springtest.json_mappings.ArticleContent;
import com.fortwoone.springtest.model.Article;
import com.fortwoone.springtest.model.ArticleLight;
import com.fortwoone.springtest.model.ReturnedArticle;
import com.fortwoone.springtest.model.User;
import com.fortwoone.springtest.repositories.ArticleRepository;
import com.fortwoone.springtest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
            @RequestBody ArticleContent content
    ){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();

        User user = (User)auth.getDetails();

        Article a = new Article();
        a.setAuthor(userRepository.findById(user.getId()).orElseThrow());
        a.setContent(content.content());
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

    @PreAuthorize("hasRole('MODERATOR') || hasRole('PUBLISHER')")
    @GetMapping(path="/all_details")
    public @ResponseBody Iterable<ReturnedArticle> getAllArticleDetails(){
        List<Article> origArticles = new ArrayList<>();
        Iterable<Article> allArticles = articleRepository.findAll();
        allArticles.iterator().forEachRemaining(origArticles::add);

        return origArticles.stream().map(ReturnedArticle::new).toList();
    }

    @PreAuthorize("hasRole('MODERATOR') || hasRole('PUBLISHER')")
    @GetMapping(path="/mine")
    public @ResponseBody Iterable<ReturnedArticle> getMyArticles(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();

        User user = (User)auth.getDetails();
        List<Article> origArticles = new ArrayList<>();
        Iterable<Article> allArticles = articleRepository.findByAuthor_Id(user.getId());
        allArticles.iterator().forEachRemaining(origArticles::add);

        return origArticles.stream().map(ReturnedArticle::new).toList();
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<ArticleLight> getAllArticles(){
        List<Article> origArticles = new ArrayList<>();
        Iterable<Article> allArticles = articleRepository.findAll();
        allArticles.iterator().forEachRemaining(origArticles::add);

        return origArticles.stream().map(ArticleLight::new).toList();
    }
}
