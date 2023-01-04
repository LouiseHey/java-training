package com.scottlogic.matcher.models;

import com.scottlogic.matcher.utility.IdGenerator;
import lombok.Getter;

@Getter
public class Order {
    private final String orderId;
    private final Account account;
    private final int price;
    private int quantity;
    private final Action action;

    public Order(Account account, int price, int quantity, Action action) {
        this(IdGenerator.generate(), account, price, quantity, action);
    }

    private Order(String orderId, Account account, int price, int quantity, Action action) {
        this.orderId = orderId;
        this.account = account;
        this.price = price;
        this.quantity = quantity;
        this.action = action;
    }

    public void removeQuantity(int removedQuantity) {
        this.quantity = this.quantity - removedQuantity;
    }
}
