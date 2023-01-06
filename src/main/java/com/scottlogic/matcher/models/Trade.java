package com.scottlogic.matcher.models;

import com.scottlogic.matcher.utility.IdGenerator;
import lombok.Getter;

@Getter
public class Trade {
    private final String tradeId;
    private final String buyOrderId;
    private final String sellOrderId;
    private final User buyUser;
    private final User sellUser;
    private final int price;
    private final int quantity;

    public Trade(Order buyOrder, Order sellOrder, int price) {
        this(buyOrder.getOrderId(),
                sellOrder.getOrderId(),
                buyOrder.getUser(),
                sellOrder.getUser(),
                price,
                Math.min(sellOrder.getQuantity(), buyOrder.getQuantity()));
    }

    public Trade(String buyOrderId, String sellOrderId, User buyUser, User sellUser, int price, int quantity) {
        this.tradeId = IdGenerator.generate();
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.buyUser = buyUser;
        this.sellUser = sellUser;
        this.price = price;
        this.quantity = quantity;
    }
}
