package com.scottlogic.matcher.service.database;

import com.scottlogic.matcher.data.UserRepository;
import com.scottlogic.matcher.data.entity.UserEntity;
import com.scottlogic.matcher.exception.ResourceCreationException;
import com.scottlogic.matcher.models.User;
import com.scottlogic.matcher.service.UserService;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDbService implements UserService {

    private final UserRepository userRepository;

    public UserDbService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public synchronized UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user with this username exists."))
                .toModel();
    }

    public synchronized void addNewUser(User newUser) {
        try {
            userRepository.insert(UserEntity.create(newUser));
        } catch (DuplicateKeyException e) {
            throw new ResourceCreationException("An exception occurred when inserting the User into the database.", e);
        }
    }
}
