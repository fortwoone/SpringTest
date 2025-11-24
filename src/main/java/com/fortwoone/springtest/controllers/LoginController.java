package com.fortwoone.springtest.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    public record LoginRequest(String name, String password){}

    private final AuthenticationManager authMan;
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    public LoginController(AuthenticationManager authMan){
        this.authMan = authMan;
    }

    @PostMapping("")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletRequest servletRequest){
        Authentication authRequest = UsernamePasswordAuthenticationToken.unauthenticated(
            request.name(), request.password()
        );
        Authentication authResponse = this.authMan.authenticate(authRequest);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authResponse);

        HttpSession session = servletRequest.getSession();
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        logoutHandler.logout(request, response, authentication);

        HttpSession session = request.getSession();

        session.removeAttribute("SPRING_SECURITY_CONTEXT");
        session.invalidate();
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);


    }
}
