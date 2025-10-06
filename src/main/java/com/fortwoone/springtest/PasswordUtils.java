package com.fortwoone.springtest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {
    public static String generateHash(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public static boolean passwordMatches(String password, String storedHash){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, storedHash);
    }
}
