package com.scottlogic.matcher.service;

import com.scottlogic.matcher.models.Trade;
import java.util.List;

public interface TradeService {
    List<Trade> saveNewTrades(List<Trade> trades);
}
