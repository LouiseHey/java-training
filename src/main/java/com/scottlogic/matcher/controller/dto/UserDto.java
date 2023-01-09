package com.scottlogic.matcher.controller.dto;

import lombok.Getter;

public class UserDto {
    @Getter
    private final String username;

    @Getter
    private final String password;

    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
