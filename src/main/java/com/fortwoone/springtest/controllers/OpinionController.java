package com.fortwoone.springtest.controllers;

import com.fortwoone.springtest.model.Article;
import com.fortwoone.springtest.model.User;
import com.fortwoone.springtest.model.UserOpinion;
import com.fortwoone.springtest.repositories.ArticleRepository;
import com.fortwoone.springtest.repositories.OpinionRepository;
import com.fortwoone.springtest.repositories.UserRepository;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/articles/opinions")
public class OpinionController {
    @Autowired
    private OpinionRepository opinionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/like_count")
    public @ResponseBody long getLikeCount(@RequestParam Article article){
        long count = 0;
        Iterable<UserOpinion> opinions = opinionRepository.findUserOpinionByArticle(article);
        for (UserOpinion opinion: opinions){
            if (opinion.getLiked() != null && opinion.getLiked()){
                count++;
            }
        }
        return count;
    }

    @GetMapping("/dislike_count")
    public @ResponseBody long getDislikeCount(@RequestParam Article article){
        long count = 0;
        Iterable<UserOpinion> opinions = opinionRepository.findUserOpinionByArticle(article);
        for (UserOpinion opinion: opinions){
            if (opinion.getLiked() != null && !opinion.getLiked()){
                count++;
            }
        }
        return count;
    }

    @GetMapping("/likes")
    public @ResponseBody Iterable<User> getArticleLikes(@RequestParam Article article){
        List<User> users = new ArrayList<>();
        Iterable<UserOpinion> opinions = opinionRepository.findUserOpinionByArticle(article);
        for (UserOpinion opinion: opinions){
            if (opinion.getLiked() != null && opinion.getLiked()){
                users.add(opinion.getUser());
            }
        }
        return users;
    }

    @GetMapping("/dislikes")
    public @ResponseBody Iterable<User> getArticleDislikes(@RequestParam Article article){
        List<User> users = new ArrayList<>();
        Iterable<UserOpinion> opinions = opinionRepository.findUserOpinionByArticle(article);
        for (UserOpinion opinion: opinions){
            if (opinion.getLiked() != null && !opinion.getLiked()){
                users.add(opinion.getUser());
            }
        }
        return users;
    }


    @PostMapping(path="/set")
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
}
