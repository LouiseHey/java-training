package com.scottlogic.matcher.controller;

import com.scottlogic.matcher.controller.dto.UserDto;
import com.scottlogic.matcher.service.UserService;
import com.scottlogic.matcher.utility.jwt.JwtTokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signin")
    public ResponseEntity<UserDto> authenticateUser(@RequestBody UserDto user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtTokenProvider.createToken(user.getUsername(), List.of()))
                .build();
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> addNewUser(@RequestBody UserDto user) {
        userService.addNewUser(user.toModel(passwordEncoder));
        return ResponseEntity.ok(user);
    }
}
