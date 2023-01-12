package com.scottlogic.matcher.models;

import lombok.Getter;

@Getter
public class Trade {
    private final String tradeId;
    private final String buyOrderId;
    private final String sellOrderId;
    private final String buyUser;
    private final String sellUser;
    private final int price;
    private final int quantity;

    public Trade(Order buyOrder, Order sellOrder, int price) {
        this(buyOrder.getOrderId(),
                sellOrder.getOrderId(),
                buyOrder.getUsername(),
                sellOrder.getUsername(),
                price,
                Math.min(sellOrder.getQuantity(), buyOrder.getQuantity()));
    }

    public Trade(String buyOrderId, String sellOrderId, String buyUser, String sellUser, int price, int quantity) {
        this(null, buyOrderId, sellOrderId, buyUser, sellUser, price, quantity);
    }

    public Trade(String id, String buyOrderId, String sellOrderId, String buyUser, String sellUser, int price, int quantity) {
        this.tradeId = id;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.buyUser = buyUser;
        this.sellUser = sellUser;
        this.price = price;
        this.quantity = quantity;
    }
}
