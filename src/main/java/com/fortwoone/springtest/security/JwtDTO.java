package com.fortwoone.springtest.security;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class JwtDTO {
    private final String token;
    private final String type = "Bearer";
    private final String username;
    private final List<String> roles;
}
