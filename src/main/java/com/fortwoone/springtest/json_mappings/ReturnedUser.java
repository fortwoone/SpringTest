package com.fortwoone.springtest.json_mappings;

import com.fortwoone.springtest.model.User;

public record ReturnedUser(int id, String name) {
    public ReturnedUser(User user){
        this(user.getId(), user.getUsername());
    }
}
