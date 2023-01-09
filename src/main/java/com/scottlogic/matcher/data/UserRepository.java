package com.scottlogic.matcher.data;

import com.scottlogic.matcher.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class UserRepository {

    private final List<User> users;

    public UserRepository() {
        this.users = List.of(new User("Louise", new BCryptPasswordEncoder().encode("Password")));
    }

    public Optional<User> findByUsername(String username) {
        for (User user : users) {
            if (Objects.equals(user.getUsername(), username)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

}
