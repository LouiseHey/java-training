package com.scottlogic.matcher.models;

import lombok.Getter;

@Getter
public class Order implements Comparable<Order> {
    private final String orderId;
    private final String username;
    private final int price;
    private int quantity;
    private final int initialQuantity;
    private final Action action;

    public Order(String username, int price, int quantity, int initialQuantity, Action action) {
        this(null, username, price, quantity, initialQuantity, action);
    }

    public Order(String orderId, String username, int price, int quantity, int initialQuantity, Action action) {
        this.orderId = orderId;
        this.username = username;
        this.price = price;
        this.quantity = quantity;
        this.initialQuantity = initialQuantity;
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
