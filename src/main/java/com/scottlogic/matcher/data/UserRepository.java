package com.scottlogic.matcher.data;

import com.scottlogic.matcher.data.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends MongoRepository<UserEntity, String> {
    UserEntity findByUsername(String username);
}
