package com.scottlogic.matcher.service;

import com.scottlogic.matcher.data.UserRepository;
import com.scottlogic.matcher.data.entity.UserEntity;
import com.scottlogic.matcher.exception.ResourceCreationException;
import com.scottlogic.matcher.models.User;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addNewUser(User newUser) {
        try {
            userRepository.insert(UserEntity.create(newUser));
        } catch (DuplicateKeyException e) {
            throw new ResourceCreationException("An exception occurred when inserting the User into the database.", e);
        }
    }
}
