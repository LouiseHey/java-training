package com.scottlogic.matcher.data.entity;

import com.scottlogic.matcher.models.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="User")
public class UserEntity {
    @Id
    public String id;

    public String username;
    public String password;

    public UserEntity() {}

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User toModel() {
        return new User(this.username, this.password);
    }
}
