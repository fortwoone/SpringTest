package com.fortwoone.springtest.repositories;

import org.springframework.data.repository.CrudRepository;

import com.fortwoone.springtest.model.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByName(String name);

    boolean existsByName(String name);
}