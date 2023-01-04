package com.scottlogic.matcher.models;

import com.scottlogic.matcher.utility.IdGenerator;
import lombok.Getter;

@Getter
public class Account {
    private final String accountId;

    public Account() {
        this.accountId = IdGenerator.generate();
    }

    public Account(String accountId) {
        this.accountId = accountId;
    }
}
