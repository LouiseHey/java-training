package com.scottlogic.matcher.models;

import com.scottlogic.matcher.utility.IdGenerator;
import lombok.Getter;

@Getter
public class Trade {
    private final String tradeId;
    private final String buyOrderId;
    private final String sellOrderId;
    private final Account buyAccount;
    private final Account sellAccount;
    private final int price;
    private final int quantity;

    public Trade(Order buyOrder, Order sellOrder, int price) {
        this(buyOrder.getOrderId(),
                sellOrder.getOrderId(),
                buyOrder.getAccount(),
                sellOrder.getAccount(),
                price,
                Math.min(sellOrder.getQuantity(), buyOrder.getQuantity()));
    }

    public Trade(String buyOrderId, String sellOrderId, Account buyAccount, Account sellAccount, int price, int quantity) {
        this.tradeId = IdGenerator.generate();
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.buyAccount = buyAccount;
        this.sellAccount = sellAccount;
        this.price = price;
        this.quantity = quantity;
    }
}
