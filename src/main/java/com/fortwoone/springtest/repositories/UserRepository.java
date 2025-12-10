package com.fortwoone.springtest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fortwoone.springtest.model.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

@SuppressWarnings("unused")
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query
    User findByName(String name);

    @Query
    boolean existsByName(String name);
}