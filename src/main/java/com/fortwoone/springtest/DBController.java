package com.fortwoone.springtest;

import java.util.ArrayList;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;

import com.fortwoone.springtest.model.Article;
import com.fortwoone.springtest.model.User;
import com.fortwoone.springtest.model.UserOpinion;
import com.fortwoone.springtest.model.UserRole;
import com.fortwoone.springtest.repositories.ArticleRepository;
import com.fortwoone.springtest.repositories.OpinionRepository;
import com.fortwoone.springtest.repositories.UserRepository;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("")
public class DBController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private OpinionRepository opinionRepository;

    private long getLikeCount(Article article){
        long count = 0;
        Iterable<UserOpinion> opinions = opinionRepository.findUserOpinionByArticle(article);
        for (UserOpinion opinion: opinions){
            if (opinion.getLiked()){
                count++;
            }
        }
        return count;
    }

    private long getDislikeCount(Article article){
        long count = 0;
        Iterable<UserOpinion> opinions = opinionRepository.findUserOpinionByArticle(article);
        for (UserOpinion opinion: opinions){
            if (!opinion.getLiked()){
                count++;
            }
        }
        return count;
    }

    private Iterable<User> getArticleLikes(Article article){
        List<User> users = new ArrayList<>();
        Iterable<UserOpinion> opinions = opinionRepository.findUserOpinionByArticle(article);
        for (UserOpinion opinion: opinions){
            if (opinion.getLiked()){
                users.add(opinion.getUser());
            }
        }
        return users;
    }

    private Iterable<User> getArticleDislikes(Article article){
        List<User> users = new ArrayList<>();
        Iterable<UserOpinion> opinions = opinionRepository.findUserOpinionByArticle(article);
        for (UserOpinion opinion: opinions){
            if (!opinion.getLiked()){
                users.add(opinion.getUser());
            }
        }
        return users;
    }

    @PostMapping(path="/users/add") // Map ONLY POST Requests
    public @ResponseBody String addNewUser(
        @RequestParam String name,
        @RequestParam String password
    ){
        User n = new User();
        n.setName(name);
        n.setPassword(PasswordUtils.generateHash(password));
        n.setRole(UserRole.PUBLISHER);
        userRepository.save(n);
        return "User saved";
    }

    @DeleteMapping(path="/users/remove")
    public @ResponseBody String removeUser(@RequestParam Integer id){
        userRepository.deleteById(id);
        return "User deleted";
    }

    @GetMapping(path="/users/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }

    @PreAuthorize("hasAnyRole(PUBLISHER, MODERATOR)")
    @PostMapping(path="/articles/add")
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

    @PostMapping(path="/articles/set_opinion")
    public @ResponseBody String setArticleOpinion(
        @RequestParam Integer articleID,
        @RequestParam Integer userID,
        @RequestParam @Nullable Boolean liked
    ){
        Iterable<UserOpinion> opinions = opinionRepository.findUserOpinionByArticle_IdAndUser_Id(articleID, userID);
        if (liked == null){
            // Remove previously set opinion if the user removes their (dis)like
            UserOpinion next = opinions.iterator().next();
            opinionRepository.delete(next);
        }
        else{
            Iterator<UserOpinion> opinionIterator = opinions.iterator();
            UserOpinion next;
            if (opinionIterator.hasNext()){
                next = opinionIterator.next();
            }
            else{
                next = new UserOpinion();
                next.setArticle(articleRepository.findById(articleID).orElseThrow());
                next.setUser(userRepository.findById(userID).orElseThrow());
            }
            next.setLiked(liked);
            opinionRepository.save(next);
        }
        return "Opinion set for article";
    }

    @DeleteMapping(path="/articles/remove")
    public @ResponseBody String deleteArticle(@RequestParam Integer articleID){
        articleRepository.deleteById(articleID);
        return "Article deleted";
    }

    @GetMapping(path="/articles/all")
    public @ResponseBody Iterable<Article> getAllArticles(){
        return articleRepository.findAll();
    }
}
