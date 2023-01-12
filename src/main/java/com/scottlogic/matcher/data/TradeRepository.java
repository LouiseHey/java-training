package com.scottlogic.matcher.data;

import com.scottlogic.matcher.data.entity.TradeEntity;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TradeRepository extends MongoRepository<TradeEntity, String> {}
