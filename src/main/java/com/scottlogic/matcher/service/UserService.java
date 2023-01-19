package com.scottlogic.matcher.service;

import com.scottlogic.matcher.models.User;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void addNewUser(User newUser);
}
