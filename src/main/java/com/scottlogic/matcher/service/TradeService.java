package com.scottlogic.matcher.service;

import com.scottlogic.matcher.data.TradeRepository;
import com.scottlogic.matcher.data.entity.TradeEntity;
import com.scottlogic.matcher.models.Trade;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TradeService {
    private final TradeRepository tradeRepository;

    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    public List<Trade> saveNewTrades(List<Trade> trades) {
        return tradeRepository.insert(trades.stream().map(TradeEntity::create).toList())
            .stream()
            .map(TradeEntity::toModel)
            .toList();
    }
}
