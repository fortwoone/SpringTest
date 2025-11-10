package com.fortwoone.springtest.repositories;

import com.fortwoone.springtest.model.Article;
import com.fortwoone.springtest.model.UserOpinion;
import org.springframework.data.repository.CrudRepository;

public interface OpinionRepository extends CrudRepository<UserOpinion, Integer>{

    Iterable<UserOpinion> findUserOpinionByArticle_Id(Integer articleId);

    Iterable<UserOpinion> findUserOpinionByArticle_IdAndUser_Id(Integer articleId, Integer userId);

    Iterable<UserOpinion> findUserOpinionByArticle(Article article);
}
