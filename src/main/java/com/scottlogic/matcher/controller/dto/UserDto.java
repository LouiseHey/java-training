package com.scottlogic.matcher.controller.dto;

import com.scottlogic.matcher.models.User;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserDto {
    @Getter
    private final String username;

    @Getter
    private final String password;

    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User toModel(PasswordEncoder passwordEncoder) {
        return new User(this.username, passwordEncoder.encode(this.password));
    }
}
