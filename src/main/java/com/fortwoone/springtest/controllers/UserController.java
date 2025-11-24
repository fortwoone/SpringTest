package com.fortwoone.springtest.controllers;

import com.fortwoone.springtest.PasswordUtils;
import com.fortwoone.springtest.model.User;
import com.fortwoone.springtest.model.UserRole;
import com.fortwoone.springtest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
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

    @DeleteMapping(path="/remove")
    public @ResponseBody String removeUser(@RequestParam Integer id){
        userRepository.deleteById(id);
        return "User deleted";
    }

    @GetMapping(path="/users/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }
}
