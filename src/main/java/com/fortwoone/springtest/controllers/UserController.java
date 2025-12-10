package com.fortwoone.springtest.controllers;

import java.util.List;

import com.fortwoone.springtest.PasswordUtils;
import com.fortwoone.springtest.json_mappings.ReturnedUser;
import com.fortwoone.springtest.model.User;
import com.fortwoone.springtest.model.UserRole;
import com.fortwoone.springtest.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public @ResponseBody ResponseEntity<String> addNewUser(
            @RequestParam String name,
            @RequestParam String password
    ){
        User n = new User();
        n.setName(name);
        n.setPassword(PasswordUtils.generateHash(password));
        n.setRole(UserRole.PUBLISHER);
        userRepository.save(n);
        return new ResponseEntity<>("User saved", HttpStatus.OK);
    }

    @DeleteMapping(path="/remove")
    public @ResponseBody ResponseEntity<String> removeUser(@RequestParam Integer id){
        userRepository.deleteById(id);
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }

    @GetMapping(path="/users/all")
    public @ResponseBody Iterable<ReturnedUser> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(ReturnedUser::new).toList();
    }
}
