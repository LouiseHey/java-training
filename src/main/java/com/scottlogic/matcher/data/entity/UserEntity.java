package com.scottlogic.matcher.data.entity;

import com.scottlogic.matcher.models.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="User")
public class UserEntity {
    @Id
    private String id;

    @Indexed(unique=true)
    private String username;

    private String password;

    public UserEntity() {}

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User toModel() {
        return new User(this.username, this.password);
    }

    public static UserEntity create(User user) {
        return new UserEntity(user.getUsername(), user.getPassword());
    }
}
