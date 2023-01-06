package com.scottlogic.matcher.models;

import com.scottlogic.matcher.utility.IdGenerator;
import lombok.Getter;

@Getter
public class User {
    private final String userId;
    private final String username;
    private final String password;

    public User(String username, String password) {
        this.userId = IdGenerator.generate();
        this.username = username;
        this.password = password;
    }

    public User(String userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }
}
