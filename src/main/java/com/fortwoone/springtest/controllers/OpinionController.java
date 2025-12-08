package com.fortwoone.springtest.controllers;

import com.fortwoone.springtest.json_mappings.ReturnedUser;
import com.fortwoone.springtest.model.Article;
import com.fortwoone.springtest.model.User;
import com.fortwoone.springtest.model.UserOpinion;
import com.fortwoone.springtest.repositories.ArticleRepository;
import com.fortwoone.springtest.repositories.OpinionRepository;
import com.fortwoone.springtest.repositories.UserRepository;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/articles/opinions")
public class OpinionController {
    public record OpinionSetRequest(int articleID, Boolean liked){}

    @Autowired
    private OpinionRepository opinionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @PreAuthorize("hasAnyRole('PUBLISHER', 'MODERATOR')")
    @GetMapping("/like_count")
    public @ResponseBody long getLikeCount(@RequestParam int articleID){
        long count = 0;
        Iterable<UserOpinion> opinions = opinionRepository.findUserOpinionByArticle_Id(articleID);
        for (UserOpinion opinion: opinions){
            if (opinion.getLiked() != null && opinion.getLiked()){
                count++;
            }
        }
        return count;
    }

    @PreAuthorize("hasAnyRole('PUBLISHER', 'MODERATOR')")
    @GetMapping("/dislike_count")
    public @ResponseBody long getDislikeCount(@RequestParam int articleID){
        long count = 0;
        Iterable<UserOpinion> opinions = opinionRepository.findUserOpinionByArticle_Id(articleID);
        for (UserOpinion opinion: opinions){
            if (opinion.getLiked() != null && !opinion.getLiked()){
                count++;
            }
        }
        return count;
    }

    @PreAuthorize("hasAnyRole('PUBLISHER', 'MODERATOR')")
    @GetMapping("/likes")
    public @ResponseBody Iterable<ReturnedUser> getArticleLikes(@RequestParam int articleID){
        List<User> users = new ArrayList<>();
        Iterable<UserOpinion> opinions = opinionRepository.findUserOpinionByArticle_Id(articleID);
        for (UserOpinion opinion: opinions){
            if (opinion.getLiked() != null && opinion.getLiked()){
                users.add(opinion.getUser());
            }
        }

        return users.stream().map(ReturnedUser::new).toList();
    }

    @PreAuthorize("hasAnyRole('PUBLISHER', 'MODERATOR')")
    @GetMapping("/dislikes")
    public @ResponseBody Iterable<ReturnedUser> getArticleDislikes(@RequestParam int articleID){
        List<User> users = new ArrayList<>();
        Iterable<UserOpinion> opinions = opinionRepository.findUserOpinionByArticle_Id(articleID);
        for (UserOpinion opinion: opinions){
            if (opinion.getLiked() != null && !opinion.getLiked()){
                users.add(opinion.getUser());
            }
        }
        return users.stream().map(ReturnedUser::new).toList();
    }

    @PreAuthorize("hasAnyRole('PUBLISHER', 'MODERATOR')")
    @PostMapping(path="/set")
    public @ResponseBody String setArticleOpinion(
            @RequestBody OpinionSetRequest request
    ){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();

        User user = (User)auth.getDetails();

        Iterable<UserOpinion> opinions = opinionRepository.findUserOpinionByArticle_IdAndUser_Id(request.articleID(), user.getId());
        if (request.liked() == null){
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
                next.setArticle(articleRepository.findById(request.articleID()).orElseThrow());
                next.setUser(userRepository.findById(user.getId()).orElseThrow());
            }
            next.setLiked(request.liked());
            opinionRepository.save(next);
        }
        return "Opinion set for article";
    }
}
