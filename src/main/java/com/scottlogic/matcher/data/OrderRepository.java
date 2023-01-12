package com.scottlogic.matcher.data;

import com.scottlogic.matcher.data.entity.OrderEntity;
import com.scottlogic.matcher.models.Action;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderEntity, String> {
    List<OrderEntity> findByAction(Action action);
}
