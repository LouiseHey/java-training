package com.scottlogic.matcher.models;

import com.scottlogic.matcher.utility.IdGenerator;
import lombok.Getter;

@Getter
public class User {
    private final String userId;

    public User() {
        this.userId = IdGenerator.generate();
    }

    public User(String accountId) {
        this.userId = accountId;
    }
}
