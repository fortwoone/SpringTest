package com.fortwoone.springtest.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenGenerator {

    private String jwtSecret = "trucmuchemachinchouettemarchebordeldemerdejenaimarre";

    private long jwtExpirationMs = 360000;

    public String generateJwtToken(Authentication authentication) {

        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Date tokenCreationDate = new Date();
        Date tokenExpirationDate = new Date(tokenCreationDate.getTime() + jwtExpirationMs);

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .subject((userPrincipal.getUsername()))
                .issuedAt(tokenCreationDate)
                .expiration(tokenExpirationDate)
                .signWith(key)
                .compact();
    }
}