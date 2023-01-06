package com.scottlogic.matcher.models;

import com.scottlogic.matcher.utility.IdGenerator;
import lombok.Getter;

@Getter
public class Order implements Comparable<Order> {
    private final String orderId;
    private final String userId;
    private final int price;
    private int quantity;
    private final Action action;

    public Order(String userId, int price, int quantity, Action action) {
        this(IdGenerator.generate(), userId, price, quantity, action);
    }

    private Order(String orderId, String userId, int price, int quantity, Action action) {
        this.orderId = orderId;
        this.userId = userId;
        this.price = price;
        this.quantity = quantity;
        this.action = action;
    }

    public void removeQuantity(int removedQuantity) {
        this.quantity = this.quantity - removedQuantity;
    }

    public boolean isQuantityZero() {
        return quantity == 0;
    }

    @Override
    public int compareTo(Order o) {
        return o.getPrice() - this.getPrice();
    }
}
