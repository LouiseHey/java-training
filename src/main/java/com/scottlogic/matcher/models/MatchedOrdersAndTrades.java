package com.scottlogic.matcher.models;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class MatchedOrdersAndTrades {
    private final List<Order> orders = new ArrayList<>();
    private final List<Trade> trades = new ArrayList<>();

    public boolean addOrder(Order order) {
        return orders.add(order);
    }

    public boolean addTrade(Trade trade) {
        return trades.add(trade);
    }
}
