package com.fortwoone.springtest.repositories;

import com.fortwoone.springtest.model.Article;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called articleRepository
// CRUD refers Create, Read, Update, Delete
// As a result, we do not need to write anything else in here.

public interface ArticleRepository extends CrudRepository<Article, Integer>{

}
